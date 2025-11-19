package com.itunesexplorer.foundation.extensions

import com.itunesexplorer.foundation.error.errorLogger
import com.itunesexplorer.core.error.DomainError
import io.ktor.client.network.sockets.ConnectTimeoutException
import io.ktor.client.network.sockets.SocketTimeoutException
import io.ktor.client.plugins.HttpRequestTimeoutException
import io.ktor.client.plugins.ResponseException
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

/**
 * Maps an [Exception] to a [DomainError] with appropriate categorization.
 *
 * Mapping strategy:
 * - Network errors (IOException, SocketException, DNS, etc.) → [DomainError.NetworkError]
 * - HTTP 404 → [DomainError.NotFoundError]
 * - HTTP 4xx → [DomainError.ClientError]
 * - HTTP 5xx → [DomainError.ServerError]
 * - Serialization errors → [DomainError.DataError]
 * - Everything else → [DomainError.UnknownError]
 *
 * @param context Optional context string for better error messages
 * @return Appropriate [DomainError] subtype based on the exception
 */
fun Exception.toDomainError(context: String = ""): DomainError {
    val contextPrefix = if (context.isNotBlank()) "$context: " else ""
    val errorMessage = this.message ?: "Unknown error occurred"

    errorLogger.error(
        "ErrorMapper",
        "${contextPrefix}Mapping exception: ${this::class.simpleName} - $errorMessage",
        this
    )

    return when (this) {
        is IOException,
        is SocketTimeoutException,
        is ConnectTimeoutException,
        is HttpRequestTimeoutException -> DomainError.NetworkError("${contextPrefix}Network error: $errorMessage")

        is ResponseException -> {
            val statusCode = this.response.status.value
            when (statusCode) {
                404 -> DomainError.NotFoundError("${contextPrefix}Resource not found")
                in 400..499 -> DomainError.ClientError(
                    message = "${contextPrefix}Client error: $errorMessage",
                    code = statusCode
                )

                in 500..599 -> DomainError.ServerError(
                    message = "${contextPrefix}Server error: $errorMessage",
                    code = statusCode
                )

                else -> DomainError.UnknownError(
                    message = "${contextPrefix}HTTP error ($statusCode): $errorMessage",
                    cause = this
                )
            }
        }

        is SerializationException -> DomainError.DataError("${contextPrefix}Data parsing error: $errorMessage")

        else -> DomainError.UnknownError(
            message = "${contextPrefix}$errorMessage",
            cause = this
        )
    }
}
