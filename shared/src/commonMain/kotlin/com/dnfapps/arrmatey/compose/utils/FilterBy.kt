package com.dnfapps.arrmatey.compose.utils

import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import com.dnfapps.arrmatey.api.arr.model.ArrMovie
import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.api.arr.model.MediaStatus
import com.dnfapps.arrmatey.model.InstanceType

enum class FilterBy(
    val iosText: String
) {
    All("all"),
    Monitored("monitored"),
    Unmonitored("unmonitored"),
    Missing("missing"),

    // Movies
    Wanted("wanted"),
    Downloaded("downloaded"),

    // Series
    ContinuingOnly("continuing_only"),
    EndedOnly("ended_only");

    companion object {
        fun typeEntries(type: InstanceType) =
            when (type) {
                InstanceType.Sonarr -> listOf(All, Monitored, Unmonitored, Missing, ContinuingOnly, EndedOnly)
                InstanceType.Radarr -> listOf(All, Monitored, Unmonitored, Missing, Wanted, Downloaded)
            }
    }
}

private fun List<ArrMedia>.applyBaseFiltering(filterBy: FilterBy) = when (filterBy) {
    FilterBy.All -> this
    FilterBy.Monitored -> filter { it.monitored }
    FilterBy.Unmonitored -> filter {!it.monitored }
    else -> this
}

fun List<ArrSeries>.applySeriesFiltering(filterBy: FilterBy) = when(filterBy) {
    FilterBy.Missing -> filter { it.episodeCount > it.episodeFileCount }
    FilterBy.ContinuingOnly -> filter { it.status == MediaStatus.Continuing }
    FilterBy.EndedOnly -> filter { it.status == MediaStatus.Ended }
    else -> applyBaseFiltering(filterBy) as List<ArrSeries>
}

fun List<ArrMovie>.applyMovieFiltering(filterBy: FilterBy) = when(filterBy) {
    FilterBy.Missing -> filter { it.monitored && it.isAvailable && it.movieFile == null }
    FilterBy.Wanted -> filter { it.monitored && it.movieFile == null }
    FilterBy.Downloaded -> filter { it.movieFile != null }
    else -> applyBaseFiltering(filterBy) as List<ArrMovie>
}

fun List<ArrMedia>.applyFiltering(type: InstanceType, filterBy: FilterBy) = when(type) {
    InstanceType.Sonarr -> (this as List<ArrSeries>).applySeriesFiltering(filterBy)
    InstanceType.Radarr -> (this as List<ArrMovie>).applyMovieFiltering(filterBy)
}