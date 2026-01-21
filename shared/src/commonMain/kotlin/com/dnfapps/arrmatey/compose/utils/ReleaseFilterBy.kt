package com.dnfapps.arrmatey.compose.utils

import com.dnfapps.arrmatey.arr.api.model.ArrRelease
import com.dnfapps.arrmatey.arr.api.model.SeriesRelease

enum class ReleaseFilterBy {
    Any,
    SeasonPack,
    SingleEpisode
}

fun List<ArrRelease>.applyFiltering(filterBy: ReleaseFilterBy) =
    (this as? List<SeriesRelease>)?.let { releases ->
        when (filterBy) {
            ReleaseFilterBy.Any -> releases
            ReleaseFilterBy.SeasonPack -> releases.filter { it.fullSeason }
            ReleaseFilterBy.SingleEpisode -> releases.filter { !it.fullSeason }
        }
    } ?: this