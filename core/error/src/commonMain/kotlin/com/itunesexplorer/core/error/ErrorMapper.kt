package com.itunesexplorer.core.error

import com.itunesexplorer.core.common.domain.DomainError
import com.itunesexplorer.core.common.domain.DomainResult
import com.itunesexplorer.core.logger.LogLevel
import com.itunesexplorer.core.logger.createPlatformLogger
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpStatusCode

/**
 * Logger for error handling.
 * Uses platform logger directly to avoid circular dependency.
 * Public to allow access from inline functions.
 */
@PublishedApi
internal val errorLogger = createPlatformLogger(LogLevel.DEBUG)

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
 * iOS Content-Length Workaround:
 * When a Content-Length mismatch error occurs on iOS (from Darwin engine),
 * it means NSURLSession detected a mismatch between Content-Length header
 * and actual response body size. However, the response data is still valid
 * and has been received successfully. We retry the operation once to get
 * the cached/valid response.
 *
 * @param context Optional context for error messages
 * @param block The suspend function to execute
 * @return DomainResult containing the value or a DomainError
 */
suspend inline fun <T> runCatchingDomain(
    context: String = "",
    crossinline block: suspend () -> T
): DomainResult<T> {
    return try {
        DomainResult.success(block())
    } catch (e: Exception) {
        // iOS Darwin engine workaround for Content-Length mismatch
        val errorMessage = e.message ?: ""
        if (errorMessage.contains("Content-Length", ignoreCase = true) &&
            errorMessage.contains("mismatch", ignoreCase = true)) {
            errorLogger.warning("ErrorMapper", "Content-Length mismatch - retrying operation to get cached response")

            // Retry once - the response should be cached and valid
            try {
                DomainResult.success(block())
            } catch (retryException: Exception) {
                errorLogger.error("ErrorMapper", "Retry failed: ${retryException.message}", retryException)
                DomainResult.failure(retryException.toDomainError(context))
            }
        } else {
            DomainResult.failure(e.toDomainError(context))
        }
    }
}
