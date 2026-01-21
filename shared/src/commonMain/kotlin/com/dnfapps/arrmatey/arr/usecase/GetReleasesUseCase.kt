package com.dnfapps.arrmatey.arr.usecase

import com.dnfapps.arrmatey.arr.api.model.ArrRelease
import com.dnfapps.arrmatey.arr.api.model.ReleaseParams
import com.dnfapps.arrmatey.instances.repository.InstanceManager
import com.dnfapps.arrmatey.client.NetworkResult
import com.dnfapps.arrmatey.arr.state.LibraryUiState
import com.dnfapps.arrmatey.instances.model.InstanceType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

class GetReleasesUseCase(
    private val instanceManager: InstanceManager
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(type: InstanceType): Flow<LibraryUiState<ArrRelease>> =
        instanceManager.getSelectedRepository(type)
            .filterNotNull()
            .flatMapLatest { repository ->
                repository.releases.map { result ->
                    when (result) {
                        null -> LibraryUiState.Initial
                        is NetworkResult.Loading -> LibraryUiState.Loading
                        is NetworkResult.Error ->
                            LibraryUiState.Error(message = result.message ?: "")

                        is NetworkResult.Success ->
                            LibraryUiState.Success(items = result.data)

                    }
                }
            }

    suspend fun fetch(type: InstanceType, params: ReleaseParams) {
        instanceManager.getSelectedRepository(type)
            .firstOrNull()
            ?.getReleases(params)
    }

    suspend fun clear(type: InstanceType) {
        instanceManager.getSelectedRepository(type)
            .firstOrNull()
            ?.clearReleases()
    }
}