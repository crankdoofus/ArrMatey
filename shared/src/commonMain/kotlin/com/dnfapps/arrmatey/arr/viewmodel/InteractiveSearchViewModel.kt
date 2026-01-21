package com.dnfapps.arrmatey.arr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnfapps.arrmatey.arr.api.model.ArrRelease
import com.dnfapps.arrmatey.arr.api.model.ReleaseParams
import com.dnfapps.arrmatey.arr.usecase.DownloadReleaseUseCase
import com.dnfapps.arrmatey.instances.usecase.GetInstanceRepositoryUseCase
import com.dnfapps.arrmatey.arr.usecase.GetReleasesUseCase
import com.dnfapps.arrmatey.arr.state.DownloadState
import com.dnfapps.arrmatey.arr.state.LibraryUiState
import com.dnfapps.arrmatey.instances.model.InstanceType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class InteractiveSearchViewModel(
    private val instanceType: InstanceType,
    private val getReleasesUseCase: GetReleasesUseCase,
    private val downloadReleaseUseCase: DownloadReleaseUseCase,
    private val getInstanceRepositoryUseCase: GetInstanceRepositoryUseCase
): ViewModel() {

    private val _releaseUiState = MutableStateFlow<LibraryUiState<ArrRelease>>(LibraryUiState.Initial)
    val releaseUiState: StateFlow<LibraryUiState<ArrRelease>> = _releaseUiState.asStateFlow()

    private val _downloadReleaseState = MutableStateFlow<DownloadState>(DownloadState.Initial)
    val downloadReleaseState: StateFlow<DownloadState> = _downloadReleaseState.asStateFlow()

    init {
        observeReleases()
        observeDownloadStatus()
    }

    private fun observeReleases() {
        viewModelScope.launch {
            getReleasesUseCase(instanceType)
                .collect { state ->
                    _releaseUiState.value = state
                }
        }
    }

    private fun observeDownloadStatus() {
        viewModelScope.launch {
            getInstanceRepositoryUseCase.observeSelected(instanceType)
                .filterNotNull()
                .collectLatest { repository ->
                    repository.downloadStatus.collect { status ->
                        _downloadReleaseState.value = status
                    }
                }
        }
    }

    fun getRelease(params: ReleaseParams) {
        viewModelScope.launch {
            getReleasesUseCase.fetch(instanceType, params)
        }
    }

    fun downloadRelease(release: ArrRelease, force: Boolean = false) {
        viewModelScope.launch {
            _downloadReleaseState.value = DownloadState.Loading(release.guid)
            downloadReleaseUseCase(instanceType, release, force)
        }
    }

    fun resetDownloadState() {
        _downloadReleaseState.value = DownloadState.Initial
    }

    override fun onCleared() {
        viewModelScope.launch {
            getReleasesUseCase.clear(instanceType)
        }
        super.onCleared()
    }
}