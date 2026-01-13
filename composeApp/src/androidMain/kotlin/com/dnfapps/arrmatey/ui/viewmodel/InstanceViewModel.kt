package com.dnfapps.arrmatey.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dnfapps.arrmatey.database.InstanceRepository
import com.dnfapps.arrmatey.model.Instance
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InstanceViewModel: ViewModel(), KoinComponent {

    private val repository: InstanceRepository by inject()

    val allInstancesFlow = repository.allInstancesFlow

    fun setSelected(instance: Instance) {
        viewModelScope.launch {
            repository.setInstanceActive(instance)
        }
    }

    fun delete(instance: Instance) {
        viewModelScope.launch {
            repository.deleteInstance(instance)
        }
    }

}