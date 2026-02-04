package com.dnfapps.arrmatey.arr.usecase

import com.dnfapps.arrmatey.arr.api.model.CommandPayload
import com.dnfapps.arrmatey.client.NetworkResult
import com.dnfapps.arrmatey.instances.model.InstanceType
import com.dnfapps.arrmatey.instances.repository.InstanceScopedRepository

class PerformAutomaticSearchUseCase {
    suspend operator fun invoke(
        mediaId: Long,
        type: InstanceType,
        repository: InstanceScopedRepository,
        episodeId: Long? = null,
        seasonNumber: Int? = null,
    ): NetworkResult<Any> {
        val payload = when (type) {
            InstanceType.Sonarr -> {
                when {
                    episodeId != null -> CommandPayload.Episode(listOf(episodeId))
                    seasonNumber != null -> CommandPayload.Season(mediaId, seasonNumber)
                    else -> CommandPayload.Series(mediaId)
                }
            }
            InstanceType.Radarr -> CommandPayload.Movie(listOf(mediaId))
        }
        return repository.executeCommand(payload)
    }
}