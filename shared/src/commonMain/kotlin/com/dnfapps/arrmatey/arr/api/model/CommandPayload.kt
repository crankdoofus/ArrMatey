package com.dnfapps.arrmatey.arr.api.model

import kotlinx.serialization.Serializable

@Serializable
sealed class CommandPayload(val name: String) {
    @Serializable
    data class Movie(val movieIds: List<Long>): CommandPayload("MoviesSearch")
    @Serializable
    data class Series(val seriesId: Long): CommandPayload("SeriesSearch")
    @Serializable
    data class Season(val seriesId: Long, val seasonNumber: Int): CommandPayload("SeasonSearch")
    @Serializable
    data class Episode(val episodeIds: List<Long>): CommandPayload("EpisodeSearch")
    @Serializable
    data class RefreshSeries(val seriesId: Long): CommandPayload("RefreshSeries")
    @Serializable
    data class RefreshMovie(val movieIds: List<Long>): CommandPayload("RefreshMovie")
    @Serializable
    data object RefreshMonitoredDownloads: CommandPayload("RefreshMonitoredDownloads")
}