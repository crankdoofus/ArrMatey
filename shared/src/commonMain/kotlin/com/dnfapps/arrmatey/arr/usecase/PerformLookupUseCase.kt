package com.dnfapps.arrmatey.arr.usecase

import com.dnfapps.arrmatey.instances.repository.InstanceManager
import com.dnfapps.arrmatey.instances.model.InstanceType
import kotlinx.coroutines.flow.firstOrNull

class PerformLookupUseCase(
    private val instanceManager: InstanceManager
) {
    suspend operator fun invoke(type: InstanceType, query: String) {
        instanceManager.getSelectedRepository(type)
            .firstOrNull()
            ?.performLookup(query)
    }

    suspend fun clear(type: InstanceType) {
        instanceManager.getSelectedRepository(type)
            .firstOrNull()
            ?.clearLookup()
    }
}