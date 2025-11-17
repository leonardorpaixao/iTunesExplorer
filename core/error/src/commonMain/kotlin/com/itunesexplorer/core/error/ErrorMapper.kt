package com.itunesexplorer.core.error

import com.itunesexplorer.core.common.domain.DomainError
import com.itunesexplorer.core.common.domain.DomainResult
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

/**
 * Convert a throwable to a DomainError.
 *
 * @param context Optional context message for better error description
 * @return Appropriate DomainError instance
 */
fun Throwable.toDomainError(context: String = ""): DomainError {
    return when (this) {
        is ServerResponseException -> DomainError.ServerError(
            message = this.message.orEmpty(),
            code = this.response.status.value
        )

        is ClientRequestException -> {
            when (this.response.status) {
                HttpStatusCode.NotFound -> DomainError.NotFoundError(
                    message = context.ifEmpty { this.message.orEmpty() }
                )
                else -> DomainError.ClientError(
                    message = this.message.orEmpty(),
                    code = this.response.status.value
                )
            }
        }

        is kotlinx.serialization.SerializationException -> DomainError.DataError(
            message = this.message.orEmpty()
        )

        else -> DomainError.UnknownError(
            message = this.message.orEmpty(),
            cause = this
        )
    }
}

/**
 * Execute a suspend block and wrap the result in a DomainResult type,
 * converting any exceptions to DomainErrors.
 *
 * @param context Optional context for error messages
 * @param block The suspend function to execute
 * @return DomainResult containing the value or a DomainError
 */
suspend inline fun <T> runCatchingDomain(
    context: String = "",
    block: () -> T
): DomainResult<T> {
    return try {
        DomainResult.success(block())
    } catch (e: Exception) {
        DomainResult.failure(e.toDomainError(context))
    }
}
