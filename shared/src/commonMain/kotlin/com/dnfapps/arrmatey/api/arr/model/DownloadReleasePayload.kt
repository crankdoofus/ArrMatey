package com.dnfapps.arrmatey.api.arr.model

import kotlinx.serialization.Serializable

@Serializable
sealed interface DownloadReleasePayload {
    val guid: String
    val indexerId: Int

    @Serializable
    data class Movie(
        override val guid: String,
        override val indexerId: Int,
        val movieId: Int?
    ) : DownloadReleasePayload

    @Serializable
    data class Season(
        override val guid: String,
        override val indexerId: Int,
        val seriesId: Int?,
        val seasonNumber: Int?
    ) : DownloadReleasePayload

    @Serializable
    data class Episode(
        override val guid: String,
        override val indexerId: Int,
        val episodeId: Int?
    ) : DownloadReleasePayload
}