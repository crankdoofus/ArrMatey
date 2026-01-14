package com.dnfapps.arrmatey.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.api.arr.model.CommandPayload
import com.dnfapps.arrmatey.api.arr.model.Episode
import com.dnfapps.arrmatey.api.arr.model.Season
import com.dnfapps.arrmatey.compose.utils.bytesAsFileSizeString
import com.dnfapps.arrmatey.entensions.Bullet
import com.dnfapps.arrmatey.extensions.formatAsRuntime
import com.dnfapps.arrmatey.navigation.ArrScreen
import com.dnfapps.arrmatey.navigation.ArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrViewModel
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
@Composable
fun SeasonHeader(
    seriesId: Long?,
    season: Season,
    episodes: List<Episode>,
    navigation: ArrTabNavigation = LocalArrTabNavigation.current
) {
    val arrViewModel = LocalArrViewModel.current
    if (arrViewModel == null) return

    val tbaLabel = stringResource(R.string.tba)
    val year = remember(episodes) {
        episodes.mapNotNull { it.airDateUtc }.minOrNull()
            ?.toLocalDateTime(TimeZone.UTC)?.date?.year?.toString()
            ?: tbaLabel
    }

    val runtime = remember(episodes) {
        val items = episodes.mapNotNull { it.runtime?.takeIf { r -> r > 0 } }
        if (items.isEmpty()) null
        else items.sorted()[items.size / 2].formatAsRuntime()
    }

    val seasonInfo = listOfNotNull(
        year, runtime, season.statistics?.sizeOnDisk?.bytesAsFileSizeString()
    )
    val infoString = seasonInfo.joinToString(Bullet)
    Text(
        text = infoString,
        fontSize = 16.sp
    )
    ReleaseDownloadButtons(
        onInteractiveClicked = {
            seriesId?.let { seriesId ->
                val destination = ArrScreen.SeriesRelease(seriesId = seriesId, seasonNumber = season.seasonNumber)
                navigation.navigateTo(destination)
            }
        },
        onAutomaticClicked = {
            seriesId?.let { seriesId ->
                val seasonSearchCommand = CommandPayload.Season(seriesId, season.seasonNumber)
                arrViewModel.command(seasonSearchCommand)
            }
        },
        automaticSearchInProgress = false,
        modifier = Modifier.fillMaxWidth().padding(top = 6.dp),
        smallSpacing = true,
        automaticSearchEnabled = episodes.any { it.monitored }
    )
}