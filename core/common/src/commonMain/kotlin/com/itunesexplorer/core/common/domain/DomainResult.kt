package com.itunesexplorer.core.common.domain

/**
 * A type that represents either a successful result with a value of type [T],
 * or a failure with a [DomainError].
 */
sealed class DomainResult<out T> {
    /**
     * Represents a successful result containing a value.
     */
    data class Success<T>(val value: T) : DomainResult<T>()

    /**
     * Represents a failed result containing an error.
     */
    data class Failure(val error: DomainError) : DomainResult<Nothing>()

    /**
     * Returns `true` if this is a [Success], `false` otherwise.
     */
    fun isSuccess(): Boolean = this is Success

    /**
     * Returns `true` if this is a [Failure], `false` otherwise.
     */
    fun isFailure(): Boolean = this is Failure

    /**
     * Returns the value if this is a [Success], or `null` if it's a [Failure].
     */
    fun getOrNull(): T? = when (this) {
        is Success -> value
        is Failure -> null
    }

    /**
     * Returns the error if this is a [Failure], or `null` if it's a [Success].
     */
    fun errorOrNull(): DomainError? = when (this) {
        is Success -> null
        is Failure -> error
    }

    /**
     * Executes the given [action] on the value if this is a [Success].
     */
    inline fun onSuccess(action: (T) -> Unit): DomainResult<T> {
        if (this is Success) {
            action(value)
        }
        return this
    }

    /**
     * Executes the given [action] on the error if this is a [Failure].
     */
    inline fun onFailure(action: (DomainError) -> Unit): DomainResult<T> {
        if (this is Failure) {
            action(error)
        }
        return this
    }

    /**
     * Transforms the value if this is a [Success], or propagates the [Failure].
     */
    inline fun <R> map(transform: (T) -> R): DomainResult<R> = when (this) {
        is Success -> Success(transform(value))
        is Failure -> this
    }

    /**
     * Applies the pattern matching approach to handle both success and failure cases.
     */
    inline fun <R> fold(
        onSuccess: (T) -> R,
        onFailure: (DomainError) -> R
    ): R = when (this) {
        is Success -> onSuccess(value)
        is Failure -> onFailure(error)
    }

    companion object {
        /**
         * Creates a [Success] result.
         */
        fun <T> success(value: T): DomainResult<T> = Success(value)

        /**
         * Creates a [Failure] result.
         */
        fun <T> failure(error: DomainError): DomainResult<T> = Failure(error)
    }
}
