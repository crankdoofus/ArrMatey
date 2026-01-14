package com.dnfapps.arrmatey.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.api.arr.model.CommandPayload
import com.dnfapps.arrmatey.api.arr.model.Episode
import com.dnfapps.arrmatey.entensions.Bullet
import com.dnfapps.arrmatey.entensions.bullet
import com.dnfapps.arrmatey.extensions.isToday
import com.dnfapps.arrmatey.extensions.isTodayOrAfter
import com.dnfapps.arrmatey.navigation.ArrScreen
import com.dnfapps.arrmatey.navigation.ArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrViewModel
import com.dnfapps.arrmatey.ui.theme.SonarrDownloading
import com.dnfapps.arrmatey.ui.viewmodel.SonarrViewModel

@Composable
fun EpisodeRow(
    episode: Episode,
    isActive: Boolean,
    progressLabel: String? = null,
    navigation: ArrTabNavigation = LocalArrTabNavigation.current
) {
    val arrViewModel = LocalArrViewModel.current
    if (arrViewModel == null || arrViewModel !is SonarrViewModel) return

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Column(
            modifier = Modifier.weight(1f),
        ) {
            val titleString = buildAnnotatedString {
                withStyle(SpanStyle(fontSize = 16.sp)) {
                    withStyle((SpanStyle(color = MaterialTheme.colorScheme.primary))) {
                        append("${episode.episodeNumber}. ")
                    }
                    withStyle(SpanStyle(fontWeight = FontWeight.Medium)) {
                        append(episode.title ?: "")
                    }
                    episode.finaleType?.let { finalType ->
                        withStyle(SpanStyle(
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.secondary
                        )) {
                            bullet()
                            append(finalType.label)
                        }
                    }
                }
            }
            Text(
                text = titleString,
                lineHeight = 16.sp,
                overflow = TextOverflow.MiddleEllipsis,
                maxLines = 1
            )

            val statusString = if (isActive) progressLabel else
                episode.episodeFile?.qualityName
                    ?: episode.airDate?.takeIf { it.isTodayOrAfter() }?.let {
                        stringResource(R.string.unaired)
                    }
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                statusString?.let {
                    Text(
                        text = statusString,
                        fontSize = 14.sp,
                        color = if (isActive) SonarrDownloading else Color.Unspecified
                    )
                } ?:
                Text(
                    text = stringResource(R.string.missing),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.error
                )

                val (weight, color) = if (episode.airDate?.isToday() == true)
                    FontWeight.Medium to MaterialTheme.colorScheme.primary
                else
                    FontWeight.Normal to Color.Unspecified
                Text(
                    text = "$Bullet${episode.formatAirDateUtc()}",
                    color = color,
                    fontWeight = weight,
                    fontSize = 14.sp
                )
            }
        }
        Icon(
            imageVector = Icons.Default.Person,
            contentDescription = null,
            modifier = Modifier.clickable {
                val destination = ArrScreen.SeriesRelease(episodeId = episode.id)
                navigation.navigateTo(destination)
            }
        )
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            modifier = Modifier.clickable {
                val payload = CommandPayload.Episode(listOf(episode.id))
                arrViewModel.command(payload)
            }
        )
        Icon(
            imageVector = if (episode.monitored) {
                Icons.Default.Bookmark
            } else {
                Icons.Default.BookmarkBorder
            },
            contentDescription = null,
            modifier = Modifier.clickable {
                arrViewModel.toggleEpisodeMonitor(episode.id)
            }
        )
    }
}