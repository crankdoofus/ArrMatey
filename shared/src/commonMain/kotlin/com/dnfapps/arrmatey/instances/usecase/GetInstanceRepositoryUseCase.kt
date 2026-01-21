package com.dnfapps.arrmatey.instances.usecase

import com.dnfapps.arrmatey.instances.repository.InstanceManager
import com.dnfapps.arrmatey.instances.repository.InstanceScopedRepository
import com.dnfapps.arrmatey.instances.model.InstanceType
import kotlinx.coroutines.flow.Flow

class GetInstanceRepositoryUseCase(
    private val instanceManager: InstanceManager
) {
    operator fun invoke(instanceId: Long): InstanceScopedRepository? =
        instanceManager.getRepository(instanceId)

    fun observeSelected(type: InstanceType): Flow<InstanceScopedRepository?> =
        instanceManager.getSelectedRepository(type)
}