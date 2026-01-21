package com.dnfapps.arrmatey.arr.state

import com.dnfapps.arrmatey.client.ErrorType
import com.dnfapps.arrmatey.datastore.InstancePreferences

sealed interface LibraryUiState<out T> {
    object Initial: LibraryUiState<Nothing>
    object Loading: LibraryUiState<Nothing>
    data class Success<T>(
        val items: List<T>,
        val preferences: InstancePreferences = InstancePreferences()
    ): LibraryUiState<T>
    data class Error(
        val message: String,
        val type: ErrorType = ErrorType.Http
    ): LibraryUiState<Nothing>
}