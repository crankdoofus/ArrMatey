package com.dnfapps.arrmatey.arr.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnfapps.arrmatey.arr.api.model.ArrDiskSpace
import com.dnfapps.arrmatey.arr.api.model.ArrHealth
import com.dnfapps.arrmatey.arr.api.model.ArrSoftwareStatus
import com.dnfapps.arrmatey.instances.model.Instance
import com.dnfapps.arrmatey.instances.repository.InstanceScopedRepository
import com.dnfapps.arrmatey.instances.usecase.GetInstanceRepositoryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ArrInstanceDashboardViewModel(
    private val instanceId: Long,
    private val getInstanceRepositoryUseCase: GetInstanceRepositoryUseCase
): ViewModel() {

    private val _softwareStatus = MutableStateFlow<ArrSoftwareStatus?>(null)
    val softwareStatus: StateFlow<ArrSoftwareStatus?> = _softwareStatus.asStateFlow()

    private val _diskSpaces = MutableStateFlow<List<ArrDiskSpace>>(emptyList())
    val diskSpaces: StateFlow<List<ArrDiskSpace>> = _diskSpaces.asStateFlow()

    private val _health = MutableStateFlow<List<ArrHealth>>(emptyList())
    val health: StateFlow<List<ArrHealth>> = _health.asStateFlow()

    private val _instance = MutableStateFlow<Instance?>(null)
    val instance: StateFlow<Instance?> = _instance.asStateFlow()

    init {
        refreshInstance()
    }

    private fun refreshInstance() {
        viewModelScope.launch {
            getInstanceRepositoryUseCase(instanceId)?.let { repository ->
                _instance.value = repository.instance
                observeRepositoryData(repository)
                repository.refreshAllMetadata()
            }
        }
    }

    private fun observeRepositoryData(repository: InstanceScopedRepository) {
        viewModelScope.launch {
            repository.softwareStatus
                .collect { _softwareStatus.value = it }
        }
        viewModelScope.launch {
            repository.diskSpace
                .collect { _diskSpaces.value = it }
        }
        viewModelScope.launch {
            repository.health
                .collect { _health.value = it }
        }
    }
}