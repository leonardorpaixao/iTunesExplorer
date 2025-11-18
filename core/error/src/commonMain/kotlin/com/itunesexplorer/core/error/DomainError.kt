package com.itunesexplorer.core.error

/**
 *
These errors are independent of specific API implementations and represent
 * business-level failure scenarios.
 */
sealed class DomainError(open val message: String) {
    data class NetworkError(override val message: String) : DomainError(message)
    data class ServerError(override val message: String, val code: Int? = null) : DomainError(message)
    data class NotFoundError(override val message: String) : DomainError(message)
    data class ClientError(override val message: String, val code: Int? = null) : DomainError(message)
    data class DataError(override val message: String) : DomainError(message)
    data class UnknownError(override val message: String, val cause: Throwable? = null) : DomainError(message)
}
