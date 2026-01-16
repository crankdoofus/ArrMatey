package com.dnfapps.arrmatey.api.arr.viewmodel

import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import com.dnfapps.arrmatey.api.arr.model.CommandPayload
import com.dnfapps.arrmatey.api.arr.model.HistoryItem
import com.dnfapps.arrmatey.api.arr.model.ArrRelease
import com.dnfapps.arrmatey.api.arr.model.QualityProfile
import com.dnfapps.arrmatey.api.arr.model.QueuePage
import com.dnfapps.arrmatey.api.arr.model.ReleaseParams
import com.dnfapps.arrmatey.api.arr.model.RootFolder
import com.dnfapps.arrmatey.api.arr.model.Tag
import com.dnfapps.arrmatey.api.client.NetworkResult
import kotlinx.coroutines.flow.StateFlow

interface ArrRepository {
    suspend fun refreshLibrary()
    suspend fun getDetails(id: Long)
    suspend fun setMonitorStatus(id: Long, monitorStatus: Boolean)
    suspend fun lookup(query: String)
    suspend fun addItem(item: ArrMedia)
    suspend fun command(payload: CommandPayload)
    suspend fun getReleases(params: ReleaseParams)
    suspend fun downloadRelease(release: ArrRelease, force: Boolean = false)

    suspend fun fetchActivityTasksSync(instanceId: Long, page: Int = 1, pageSize: Int = 100): NetworkResult<QueuePage>
    suspend fun getItemHistory(id: Long, page: Int = 1, pageSize: Int = 100)

    val uiState: StateFlow<LibraryUiState<ArrMedia>>
    val detailUiState: StateFlow<DetailsUiState<ArrMedia>>
    val lookupUiState: StateFlow<LibraryUiState<ArrMedia>>
    val addItemUiState: StateFlow<DetailsUiState<ArrMedia>>
    val releasesUiState: StateFlow<LibraryUiState<ArrRelease>>
    val downloadReleaseState: StateFlow<DownloadState>

    val automaticSearchIds: StateFlow<List<Long>>
    val automaticSearchResult: StateFlow<Boolean?>

    val itemHistoryMap: StateFlow<Map<Long, List<HistoryItem>>>
    val itemHistoryRefreshing: StateFlow<Boolean>

    val qualityProfiles: StateFlow<List<QualityProfile>>
    val rootFolders: StateFlow<List<RootFolder>>
    val tags: StateFlow<List<Tag>>

}

sealed interface DownloadState {
    data object Initial: DownloadState
    data class Loading(val guid: String): DownloadState
    data object Success: DownloadState
    data object Error: DownloadState
}