package com.dnfapps.arrmatey.arr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.arr.state.LibraryUiState
import com.dnfapps.arrmatey.arr.usecase.GetLibraryUseCase
import com.dnfapps.arrmatey.client.OperationStatus
import com.dnfapps.arrmatey.compose.utils.FilterBy
import com.dnfapps.arrmatey.compose.utils.SortBy
import com.dnfapps.arrmatey.compose.utils.SortOrder
import com.dnfapps.arrmatey.datastore.InstancePreferences
import com.dnfapps.arrmatey.instances.model.InstanceData
import com.dnfapps.arrmatey.instances.model.InstanceType
import com.dnfapps.arrmatey.instances.repository.InstanceScopedRepository
import com.dnfapps.arrmatey.instances.usecase.GetInstanceRepositoryUseCase
import com.dnfapps.arrmatey.instances.usecase.UpdatePreferencesUseCase
import com.dnfapps.arrmatey.ui.theme.ViewType
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.launch

class ArrMediaViewModel(
    private val instanceType: InstanceType,
    private val getInstanceRepositoryUseCase: GetInstanceRepositoryUseCase,
    private val getLibraryUseCase: GetLibraryUseCase,
    private val updatePreferencesUseCase: UpdatePreferencesUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow<LibraryUiState<ArrMedia>>(LibraryUiState.Initial)
    val uiState: StateFlow<LibraryUiState<ArrMedia>> = _uiState.asStateFlow()

    private val _instanceData = MutableStateFlow<InstanceData?>(null)
    val instanceData: StateFlow<InstanceData?> = _instanceData.asStateFlow()

    private val _addItemStatus = MutableStateFlow<OperationStatus>(OperationStatus.Idle)
    val addItemStatus: StateFlow<OperationStatus> = _addItemStatus.asStateFlow()

    private var currentRepository: InstanceScopedRepository? = null

    init {
        observeSelectedInstance()
    }

    private fun observeSelectedInstance() {
        viewModelScope.launch {
            getInstanceRepositoryUseCase.observeSelected(instanceType)
                .filterNotNull()
                .collectLatest { repository ->
                    currentRepository = repository

                    observeInstanceData(repository)
                    observeLibrary(repository.instance.id)

                    repository.refreshAllMetadata()
                }
        }
    }

    private fun observeLibrary(instanceId: Long) {
        viewModelScope.launch {
            getLibraryUseCase(instanceId)
                .collect { state ->
                    _uiState.value = state
                }
        }
    }

    private fun observeInstanceData(repository: InstanceScopedRepository) {
        viewModelScope.launch {
            combine(
                repository.qualityProfiles,
                repository.rootFolders,
                repository.tags
            ) { profiles, folders, tags ->
                InstanceData(
                    qualityProfiles = profiles,
                    rootFolders = folders,
                    tags = tags
                )
            }.collect { data ->
                _instanceData.value = data
            }
        }
    }

    fun executeAutomaticSearch(seriesId: Long) {
        viewModelScope.launch {
            currentRepository?.executeAutomaticSearch(seriesId)
        }
    }

    fun updateViewType(viewType: ViewType) {
        safeSavePreference { it.copy(viewType = viewType) }
    }

    fun updateSortBy(sortBy: SortBy) {
        safeSavePreference { it.copy(sortBy = sortBy) }
    }

    fun updateSortOrder(sortOrder: SortOrder) {
        safeSavePreference { it.copy(sortOrder = sortOrder) }
    }

    fun updateFilterBy(filterBy: FilterBy) {
        safeSavePreference { it.copy(filterBy = filterBy) }
    }

    private fun safeSavePreference(transform: (InstancePreferences) -> InstancePreferences) {
        viewModelScope.launch {
            val repository = currentRepository ?: return@launch
            val currentState = _uiState.value as? LibraryUiState.Success ?: return@launch
            val preferences = currentState.preferences

            val updatedPreferences = transform(preferences)
            updatePreferencesUseCase.savePreferences(repository.instance.id, updatedPreferences)
        }
    }

    fun refresh() {
        viewModelScope.launch {
            currentRepository?.refreshLibrary()
        }
    }
}