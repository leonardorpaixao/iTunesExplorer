package com.itunesexplorer.foundation.extensions

import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.core.error.DomainResult


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
