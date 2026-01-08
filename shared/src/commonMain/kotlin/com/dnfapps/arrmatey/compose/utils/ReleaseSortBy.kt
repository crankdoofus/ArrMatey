package com.dnfapps.arrmatey.compose.utils

import com.dnfapps.arrmatey.api.arr.model.IArrRelease

enum class ReleaseSortBy {
    Weight,
    Age,
    Quality,
    Seeders,
    FileSize,
    CustomScore
}

fun List<IArrRelease>.applySorting(sortBy: ReleaseSortBy, sortOrder: SortOrder) = when(sortBy) {
    ReleaseSortBy.Weight -> sortedBy { it.releaseWeight }
    ReleaseSortBy.Age -> sortedBy { it.ageMinutes }
    ReleaseSortBy.Quality -> sortedBy { it.quality.quality.resolution }
    ReleaseSortBy.Seeders -> sortedBy { it.seeders }
    ReleaseSortBy.FileSize -> sortedBy { it.size }
    ReleaseSortBy.CustomScore -> sortedBy { it.customFormatScore }
}.let { when(sortOrder) {
    SortOrder.Asc -> it
    SortOrder.Desc -> it.reversed()
} }