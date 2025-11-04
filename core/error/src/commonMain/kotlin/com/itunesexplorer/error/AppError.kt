package com.itunesexplorer.error

sealed class AppError : Exception() {
    data class NetworkError(override val message: String) : AppError()
    data class ApiError(val code: Int, override val message: String) : AppError()
    data class UnknownError(override val message: String) : AppError()
    data object NoInternetConnection : AppError()
    data object Timeout : AppError()
    data object EmptyResponse : AppError()
}

sealed class Result<out T> {
    data class Success<T>(val data: T) : Result<T>()
    data class Error(val error: AppError) : Result<Nothing>()

    inline fun <R> map(transform: (T) -> R): Result<R> = when (this) {
        is Success -> Success(transform(data))
        is Error -> this
    }

    inline fun onSuccess(action: (T) -> Unit): Result<T> {
        if (this is Success) action(data)
        return this
    }

    inline fun onError(action: (AppError) -> Unit): Result<T> {
        if (this is Error) action(error)
        return this
    }
}

suspend fun <T> safeCall(
    call: suspend () -> T
): Result<T> = try {
    Result.Success(call())
} catch (e: Exception) {
    Result.Error(
        when (e) {
            is AppError -> e
            else -> AppError.UnknownError(e.message ?: "Unknown error occurred")
        }
    )
}
