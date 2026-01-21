package com.dnfapps.arrmatey.client

sealed interface NetworkResult<out T> {
    object Loading : NetworkResult<Nothing>
    data class Success<T>(val data: T): NetworkResult<T>
    data class Error(
        val code: Int? = null,
        val message: String? = null,
        val cause: Throwable? = null
    ): NetworkResult<Nothing>

    fun <R> map(transform: (T) -> R): NetworkResult<R> {
        return when (this) {
            is Loading -> Loading
            is Error -> Error(code, message, cause)
            is Success -> Success(transform(data))
        }
    }
}

suspend fun <T> NetworkResult<T>.onSuccess(action: suspend (T) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Success) action(data)
    return this
}

suspend fun <T> NetworkResult<T>.onError(action: suspend (Int?, String?, Throwable?) -> Unit): NetworkResult<T> {
    if (this is NetworkResult.Error) action(code, message, cause)
    return this
}