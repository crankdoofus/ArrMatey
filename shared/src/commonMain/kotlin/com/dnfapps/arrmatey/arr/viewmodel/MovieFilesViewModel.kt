package com.dnfapps.arrmatey.arr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnfapps.arrmatey.arr.state.MovieFilesState
import com.dnfapps.arrmatey.arr.usecase.GetMovieFilesUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MovieFilesViewModel(
    private val movieId: Long,
    private val getMovieFilesUseCase: GetMovieFilesUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(MovieFilesState())
    val uiState: StateFlow<MovieFilesState> = _uiState.asStateFlow()

    init {
        observeMovieFiles()
        refreshHistory()
    }

    private fun observeMovieFiles() {
        viewModelScope.launch {
            getMovieFilesUseCase(movieId)
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    fun refreshHistory() {
        viewModelScope.launch {
            getMovieFilesUseCase.refreshHistory(movieId)
        }
    }
}