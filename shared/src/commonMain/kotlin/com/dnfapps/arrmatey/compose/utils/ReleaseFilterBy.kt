package com.dnfapps.arrmatey.compose.utils

import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.api.arr.model.IArrRelease
import com.dnfapps.arrmatey.api.arr.model.SeriesRelease

enum class ReleaseFilterBy {
    Any,
    SeasonPack,
    SingleEpisode
}

fun List<IArrRelease>.applyFiltering(filterBy: ReleaseFilterBy) =
    (this as? List<SeriesRelease>)?.let { releases ->
        when (filterBy) {
            ReleaseFilterBy.Any -> releases
            ReleaseFilterBy.SeasonPack -> releases.filter { it.fullSeason }
            ReleaseFilterBy.SingleEpisode -> releases.filter { !it.fullSeason }
        }
    } ?: this