package com.itunesexplorer.core.error

import com.itunesexplorer.core.logger.Logger
import io.ktor.client.network.sockets.*
import io.ktor.client.plugins.*
import io.ktor.utils.io.errors.*
import kotlinx.io.IOException
import kotlinx.serialization.SerializationException

/**
 * Global logger for error mapping operations.
 * Uses the same logger instance as the rest of the application.
 */
private val errorLogger: Logger by lazy {
    com.itunesexplorer.core.logger.createPlatformLogger(
        com.itunesexplorer.core.logger.LogLevel.ERROR
    )
}

/**
 * Wraps a suspend operation in a try-catch block and maps exceptions to [DomainError].
 *
 * Usage:
 * ```kotlin
 * suspend fun getUser(id: String): DomainResult<User> = runCatchingDomain {
 *     api.fetchUser(id)
 * }
 * ```
 *
 * @param context Optional context string for logging
 * @param block The suspend operation to execute
 * @return [DomainResult.Success] if the operation succeeds, [DomainResult.Failure] otherwise
 */
suspend inline fun <T> runCatchingDomain(
    context: String = "",
    crossinline block: suspend () -> T
): DomainResult<T> {
    return try {
        DomainResult.success(block())
    } catch (e: Exception) {
        DomainResult.failure(e.toDomainError(context))
    }
}

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

    errorLogger.error("ErrorMapper", "${contextPrefix}Mapping exception: ${this::class.simpleName} - $errorMessage", this)

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
