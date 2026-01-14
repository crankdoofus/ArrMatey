package com.dnfapps.arrmatey.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.api.arr.model.Episode
import com.dnfapps.arrmatey.compose.components.DetailHeaderBanner
import com.dnfapps.arrmatey.compose.components.EpisodePosterItem
import com.dnfapps.arrmatey.entensions.Bullet

@Composable
fun EpisodeDetailsHeader(episode: Episode, series: ArrSeries) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        DetailHeaderBanner(episode.getBanner()?.remoteUrl, height = 300.dp)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 170.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp),
            verticalAlignment = Alignment.Bottom
        ) {
            EpisodePosterItem(episode)

            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = episode.displayTitle,
                    fontSize = 38.sp,
                    fontWeight = FontWeight.Medium,
                    lineHeight = 42.sp,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = series.title,
                    fontSize = 18.sp
                )
                val statusRow = listOfNotNull(
                    episode.seasonEpLabel,
                    episode.runtimeString,
                    episode.formatAirDateUtc()
                ).joinToString(Bullet)
                Text(
                    text = statusRow,
                    fontSize = 14.sp
                )
            }
        }
    }
}