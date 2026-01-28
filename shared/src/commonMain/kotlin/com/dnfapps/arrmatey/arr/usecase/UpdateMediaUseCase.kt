package com.dnfapps.arrmatey.arr.usecase

import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.client.NetworkResult
import com.dnfapps.arrmatey.instances.repository.InstanceScopedRepository

class UpdateMediaUseCase {
    suspend operator fun invoke(
        item: ArrMedia,
        repository: InstanceScopedRepository
    ): NetworkResult<ArrMedia> {
        return repository.updateMediaItem(item)
    }

    suspend fun edit(
        item: ArrMedia,
        moveFiles: Boolean,
        repository: InstanceScopedRepository
    ): NetworkResult<Unit> {
        return repository.editMediaItem(item, moveFiles)
    }
}