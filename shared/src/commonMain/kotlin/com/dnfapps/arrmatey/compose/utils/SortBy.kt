package com.dnfapps.arrmatey.compose.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowCircleDown
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.SortByAlpha
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Tv
import androidx.compose.ui.graphics.vector.ImageVector
import arrmatey.shared.generated.resources.Res
import arrmatey.shared.generated.resources.added
import arrmatey.shared.generated.resources.digital_release
import arrmatey.shared.generated.resources.file_size
import arrmatey.shared.generated.resources.grabbed
import arrmatey.shared.generated.resources.next_airing
import arrmatey.shared.generated.resources.previous_airing
import arrmatey.shared.generated.resources.rating
import arrmatey.shared.generated.resources.sort_ascending
import arrmatey.shared.generated.resources.sort_descending
import arrmatey.shared.generated.resources.title
import arrmatey.shared.generated.resources.year
import com.dnfapps.arrmatey.api.arr.model.AnyArrMedia
import com.dnfapps.arrmatey.api.arr.model.ArrMovie
import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.compose.icons.Hard_drive
import com.dnfapps.arrmatey.model.InstanceType
import org.jetbrains.compose.resources.StringResource

enum class SortBy(
    val iosIcon: String,
    val androidIcon: ImageVector,
    val textKey: String
) {
    Title("textformat", Icons.Default.SortByAlpha, "title"),
    Year("calendar", Icons.Default.CalendarMonth, "year"),
    Added("clock.fill", Icons.Filled.Schedule, "added"),
    Rating("star.fill", Icons.Filled.Star, "rating"),
    FileSize("opticaldiscdrive.fill", Hard_drive, "file_size"),

    // Movies
    Grabbed("arrow.down.circle.fill", Icons.Default.ArrowCircleDown, "grabbed"),
    DigitalRelease("play.tv", Icons.Default.Tv, "digital_release"),

    // TV
    NextAiring("clock", Icons.Default.Schedule, "next_airing"),
    PreviousAiring("clock.arrow.trianglehead.counterclockwise.rotate.90", Icons.Default.History, "previous_airing");

    companion object {

        private val sonarrOps by lazy {
            listOf(Title, Year, Added, Rating, FileSize, NextAiring, PreviousAiring)
        }
        private val radarrOps by lazy {
            listOf(Title, Year, Added, Rating, FileSize, Grabbed, DigitalRelease)
        }
        fun typeEntries(type: InstanceType) =
            when (type) {
                InstanceType.Sonarr -> sonarrOps
                InstanceType.Radarr -> radarrOps
            }

    }
}

enum class SortOrder(
    val iosIcon: String,
    val iosText: String,
    val androidIcon: ImageVector
) {
    Asc("arrow.up", "sort_ascending", Icons.Default.ArrowUpward),
    Desc("arrow.down", "sort_descending", Icons.Default.ArrowDownward)
}

private fun List<AnyArrMedia>.applyBaseSorting(sortBy: SortBy, order: SortOrder) = when(sortBy) {
    SortBy.Title -> if (order == SortOrder.Asc) sortedBy { it.sortTitle?.lowercase() } else sortedByDescending { it.sortTitle?.lowercase() }
    SortBy.Year -> if (order == SortOrder.Asc) sortedBy { it.year } else sortedByDescending { it.year }
    SortBy.Added -> if (order == SortOrder.Asc) sortedBy { it.added } else sortedByDescending { it.added }
    SortBy.Rating -> if (order == SortOrder.Asc) sortedBy { it.ratingScore() } else sortedByDescending { it.ratingScore() }
    SortBy.FileSize -> if (order == SortOrder.Asc) sortedBy { it.statistics.sizeOnDisk } else sortedByDescending { it.statistics.sizeOnDisk }
    else -> this
}

fun List<ArrSeries>.applySeriesSorting(sortBy: SortBy, order: SortOrder = SortOrder.Asc) = when(sortBy) {
    SortBy.NextAiring -> if (order == SortOrder.Asc) sortedBy { it.nextAiring } else sortedByDescending { it.nextAiring }
    SortBy.PreviousAiring -> if (order == SortOrder.Asc) sortedBy { it.previousAiring } else sortedByDescending { it.previousAiring }
    else -> applyBaseSorting(sortBy, order) as List<ArrSeries>
}

fun List<ArrMovie>.applyMovieSorting(sortBy: SortBy, order: SortOrder = SortOrder.Asc) = when(sortBy) {
    SortBy.Grabbed -> if (order == SortOrder.Asc) sortedBy { it.movieFile?.dateAdded } else sortedByDescending { it.movieFile?.dateAdded }
    SortBy.DigitalRelease -> if (order == SortOrder.Asc) sortedBy { it.digitalRelease } else sortedByDescending { it.digitalRelease }
    else -> applyBaseSorting(sortBy, order) as List<ArrMovie>
}

fun List<AnyArrMedia>.applySorting(type: InstanceType, sortBy: SortBy, order: SortOrder = SortOrder.Asc) = when(type) {
    InstanceType.Sonarr -> (this as List<ArrSeries>).applySeriesSorting(sortBy, order)
    InstanceType.Radarr -> (this as List<ArrMovie>).applyMovieSorting(sortBy, order)
}