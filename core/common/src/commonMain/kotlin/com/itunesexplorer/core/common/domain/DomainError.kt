package com.itunesexplorer.core.common.domain

/**
 * Sealed class representing domain-level errors that can occur across features.
 * These errors are independent of specific API implementations and represent
 * business-level failure scenarios.
 */
sealed class DomainError {
    /**
     * Network-related errors (no connection, timeout, DNS failure, etc.)
     */
    data class NetworkError(val message: String) : DomainError()

    /**
     * Server-side errors (5xx responses, server unavailable)
     */
    data class ServerError(val message: String, val code: Int? = null) : DomainError()

    /**
     * Resource not found (404, empty results)
     */
    data class NotFoundError(val message: String) : DomainError()

    /**
     * Client-side errors (4xx responses except 404)
     */
    data class ClientError(val message: String, val code: Int? = null) : DomainError()

    /**
     * Data parsing or validation errors
     */
    data class DataError(val message: String) : DomainError()

    /**
     * Unknown or unexpected errors
     */
    data class UnknownError(val message: String, val cause: Throwable? = null) : DomainError()
}
