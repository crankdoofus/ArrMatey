package com.dnfapps.arrmatey.api.arr

import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import com.dnfapps.arrmatey.api.arr.model.ArrMovie
import com.dnfapps.arrmatey.api.arr.model.ExtraFile
import com.dnfapps.arrmatey.api.arr.model.MonitoredResponse
import com.dnfapps.arrmatey.api.arr.model.MovieRelease
import com.dnfapps.arrmatey.api.arr.model.RadarrHistoryItem
import com.dnfapps.arrmatey.api.arr.model.ReleaseParams
import com.dnfapps.arrmatey.api.client.NetworkResult
import com.dnfapps.arrmatey.api.client.safeGet
import com.dnfapps.arrmatey.api.client.safePost
import com.dnfapps.arrmatey.api.client.safePut
import com.dnfapps.arrmatey.model.Instance
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.contentType
import kotlinx.serialization.json.JsonPrimitive
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.putJsonArray

class RadarrClient(instance: Instance): BaseArrClient(instance) {

    override suspend fun getLibrary(): NetworkResult<List<ArrMovie>> {
        val resp = httpClient.safeGet<List<ArrMovie>>("api/v3/movie")
        return resp
    }

    override suspend fun getDetail(id: Long): NetworkResult<ArrMovie> {
        val resp = httpClient.safeGet<ArrMovie>("api/v3/movie/$id")
        return resp
    }

    override suspend fun update(item: ArrMedia): NetworkResult<ArrMovie> {
        val resp = httpClient.safePut<ArrMovie>("api/v3/move/${item.id}") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
        return resp
    }

    override suspend fun setMonitorStatus(id: Long, monitorStatus: Boolean): NetworkResult<List<MonitoredResponse>> {
        val resp = httpClient.safePut<List<MonitoredResponse>>("api/v3/movie/editor") {
            contentType(ContentType.Application.Json)

            val body = buildJsonObject {
                put("monitored", JsonPrimitive(monitorStatus))
                putJsonArray("movieIds") {
                    add(JsonPrimitive(id))
                }
            }
            setBody(body)
        }
        return resp
    }

    override suspend fun lookup(query: String): NetworkResult<List<ArrMovie>> {
        val resp = httpClient.safeGet<List<ArrMovie>>("api/v3/movie/lookup?term=$query")
        return resp
    }

    override suspend fun addItemToLibrary(item: ArrMedia): NetworkResult<ArrMovie> {
        val resp = httpClient.safePost<ArrMovie>("api/v3/movie") {
            contentType(ContentType.Application.Json)
            setBody(item)
        }
        return resp
    }

    override suspend fun getReleases(params: ReleaseParams): NetworkResult<List<MovieRelease>> {
        return when (params) {
            is ReleaseParams.Movie ->
                httpClient.safeGet<List<MovieRelease>>("api/v3/release?movieId=${params.movieId}")
            else -> NetworkResult.UnexpectedError(
                cause = IllegalStateException("Params must be ReleaseParams.Movie: $params")
            )
        }
    }

    override suspend fun getItemHistory(id: Long, page: Int, pageSize: Int): NetworkResult<List<RadarrHistoryItem>> {
        val query = "?movieId=$id&page=$page&pageSize=$pageSize"
        val resp = httpClient.safeGet<List<RadarrHistoryItem>>("api/v3/history/movie$query")
        return resp
    }

    suspend fun getMovieExtraFile(id: Long): NetworkResult<List<ExtraFile>> {
        val resp = httpClient.safeGet<List<ExtraFile>>("api/v3/extrafile?movieId=$id")
        return resp
    }

}