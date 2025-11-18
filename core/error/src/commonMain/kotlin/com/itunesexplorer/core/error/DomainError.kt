package com.itunesexplorer.core.error

/**
 *
These errors are independent of specific API implementations and represent
 * business-level failure scenarios.
 */
sealed class DomainError {
    data class NetworkError(val message: String) : DomainError()
    data class ServerError(val message: String, val code: Int? = null) : DomainError()
    data class NotFoundError(val message: String) : DomainError()
    data class ClientError(val message: String, val code: Int? = null) : DomainError()
    data class DataError(val message: String) : DomainError()
    data class UnknownError(val message: String, val cause: Throwable? = null) : DomainError()
}
