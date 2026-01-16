package com.dnfapps.arrmatey.api.arr

import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import com.dnfapps.arrmatey.api.arr.model.ArrRelease
import com.dnfapps.arrmatey.api.arr.model.CommandPayload
import com.dnfapps.arrmatey.api.arr.model.CommandResponse
import com.dnfapps.arrmatey.api.arr.model.HistoryItem
import com.dnfapps.arrmatey.api.arr.model.MonitoredResponse
import com.dnfapps.arrmatey.api.arr.model.QualityProfile
import com.dnfapps.arrmatey.api.arr.model.QueuePage
import com.dnfapps.arrmatey.api.arr.model.ReleaseParams
import com.dnfapps.arrmatey.api.arr.model.RootFolder
import com.dnfapps.arrmatey.api.arr.model.Tag
import com.dnfapps.arrmatey.api.client.NetworkResult

interface ArrClient {
    suspend fun getLibrary(): NetworkResult<List<ArrMedia>>
    suspend fun getDetail(id: Long): NetworkResult<ArrMedia>
    suspend fun update(item: ArrMedia): NetworkResult<ArrMedia>
    suspend fun setMonitorStatus(id: Long, monitorStatus: Boolean): NetworkResult<List<MonitoredResponse>>
    suspend fun lookup(query: String): NetworkResult<List<ArrMedia>>
    suspend fun getQualityProfiles(): NetworkResult<List<QualityProfile>>
    suspend fun getRootFolders(): NetworkResult<List<RootFolder>>
    suspend fun getTags(): NetworkResult<List<Tag>>
    suspend fun addItemToLibrary(item: ArrMedia): NetworkResult<ArrMedia>
    suspend fun command(payload: CommandPayload): NetworkResult<CommandResponse>
    suspend fun getReleases(params: ReleaseParams): NetworkResult<List<ArrRelease>>
    suspend fun fetchActivityTasks(instanceId: Long, page: Int, pageSize: Int): NetworkResult<QueuePage>
    suspend fun getItemHistory(id: Long, page: Int, pageSize: Int): NetworkResult<List<HistoryItem>>

}