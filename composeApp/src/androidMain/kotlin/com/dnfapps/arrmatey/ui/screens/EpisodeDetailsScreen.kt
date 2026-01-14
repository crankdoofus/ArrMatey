package com.dnfapps.arrmatey.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.api.arr.model.CommandPayload
import com.dnfapps.arrmatey.api.arr.model.Episode
import com.dnfapps.arrmatey.entensions.copy
import com.dnfapps.arrmatey.entensions.headerBarColors
import com.dnfapps.arrmatey.navigation.ArrScreen
import com.dnfapps.arrmatey.navigation.ArrTabNavigation
import com.dnfapps.arrmatey.ui.components.EpisodeDetailsHeader
import com.dnfapps.arrmatey.ui.components.FileCard
import com.dnfapps.arrmatey.ui.components.HistoryItemView
import com.dnfapps.arrmatey.ui.components.ItemDescriptionCard
import com.dnfapps.arrmatey.ui.components.OverlayTopAppBar
import com.dnfapps.arrmatey.ui.components.ReleaseDownloadButtons
import com.dnfapps.arrmatey.ui.tabs.LocalArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrViewModel
import com.dnfapps.arrmatey.ui.viewmodel.ArrViewModel
import com.dnfapps.arrmatey.ui.viewmodel.SonarrViewModel

@Composable
fun EpisodeDetailsScreen(
    series: ArrSeries,
    episode: Episode,
    navigation: ArrTabNavigation = LocalArrTabNavigation.current,
    arrViewModel: ArrViewModel? = LocalArrViewModel.current
) {
    if (arrViewModel == null || arrViewModel !is SonarrViewModel) {
        navigation.popBackStack()
        return
    }

    val scrollState = rememberScrollState()

    val itemHistoryMap by arrViewModel.itemHistoryMap.collectAsStateWithLifecycle()
    val episodeHistoryItems by remember { derivedStateOf { itemHistoryMap[episode.id] ?: emptyList() } }

    var isMonitored by remember { mutableStateOf(episode.monitored) }

    LaunchedEffect(episode) {
        arrViewModel.getItemHistory(episode.id)
    }

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues.copy(top = 0.dp, bottom = 0.dp))
                .fillMaxSize()
        ) {
            Column(
                modifier = Modifier.verticalScroll(scrollState),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                EpisodeDetailsHeader(episode, series)

                Column(
                    modifier = Modifier.padding(horizontal = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    episode.overview?.let { overview ->
                        ItemDescriptionCard(overview)
                    }

                    ReleaseDownloadButtons(
                        onInteractiveClicked = {
                            val destination = ArrScreen.SeriesRelease(episodeId = episode.id)
                            navigation.navigateTo(destination)
                        },
                        onAutomaticClicked = {
                            val payload = CommandPayload.Episode(listOf(episode.id))
                            arrViewModel.command(payload)
                        },
                        automaticSearchEnabled = episode.monitored
                    )

                    Text(
                        text = stringResource(R.string.files),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                    episode.episodeFile?.let { file ->
                        FileCard(file)
                    }

                    Text(
                        text = stringResource(R.string.history),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Medium
                    )
                    episodeHistoryItems.forEach { historyItem ->
                        HistoryItemView(historyItem)
                    }
                    if (episodeHistoryItems.isEmpty()) {
                        Text(stringResource(R.string.no_history))
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            OverlayTopAppBar(
                scrollState = scrollState,
                modifier = Modifier.align(Alignment.TopCenter),
                navigationIcon = {
                    IconButton(
                        onClick = { navigation.popBackStack() },
                        colors = IconButtonDefaults.headerBarColors()
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = stringResource(R.string.back)
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            arrViewModel.toggleEpisodeMonitor(episode.id)
                            isMonitored = !isMonitored
                        },
                        colors = IconButtonDefaults.headerBarColors()
                    ) {
                        Icon(
                            imageVector = if (isMonitored) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                            contentDescription = null
                        )
                    }
                }
            )
        }
    }
}