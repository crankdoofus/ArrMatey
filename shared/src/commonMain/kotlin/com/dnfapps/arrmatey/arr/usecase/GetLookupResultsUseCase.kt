package com.dnfapps.arrmatey.arr.usecase

import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.arr.state.LibraryUiState
import com.dnfapps.arrmatey.client.NetworkResult
import com.dnfapps.arrmatey.instances.model.InstanceType
import com.dnfapps.arrmatey.instances.repository.InstanceManager
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map

class GetLookupResultsUseCase(
    private val instanceManager: InstanceManager
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    operator fun invoke(type: InstanceType): Flow<LibraryUiState<ArrMedia>> =
        instanceManager.getSelectedRepository(type)
            .filterNotNull()
            .flatMapLatest { repository ->
                repository.lookupResults.map { result ->
                    when (result) {
                        null -> LibraryUiState.Initial
                        is NetworkResult.Loading -> LibraryUiState.Loading
                        is NetworkResult.Error -> LibraryUiState.Error(result.message ?: "")
                        is NetworkResult.Success ->
                            LibraryUiState.Success(items = result.data)
                    }
                }
            }
}