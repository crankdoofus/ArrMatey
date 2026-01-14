package com.dnfapps.arrmatey.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.LoadingIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.dnfapps.arrmatey.R
import com.dnfapps.arrmatey.api.arr.model.ArrMovie
import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.api.arr.viewmodel.DetailsUiState
import com.dnfapps.arrmatey.entensions.copy
import com.dnfapps.arrmatey.entensions.headerBarColors
import com.dnfapps.arrmatey.navigation.ArrTabNavigation
import com.dnfapps.arrmatey.ui.components.DetailsHeader
import com.dnfapps.arrmatey.ui.components.InfoArea
import com.dnfapps.arrmatey.ui.components.ItemDescriptionCard
import com.dnfapps.arrmatey.ui.components.MovieFileView
import com.dnfapps.arrmatey.ui.components.OverlayTopAppBar
import com.dnfapps.arrmatey.ui.components.SeasonsArea
import com.dnfapps.arrmatey.ui.components.UpcomingDateView
import com.dnfapps.arrmatey.ui.tabs.LocalArrTabNavigation
import com.dnfapps.arrmatey.ui.tabs.LocalArrViewModel

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun MediaDetailsScreen(
    id: Long,
    navigation: ArrTabNavigation = LocalArrTabNavigation.current
) {
    val arrViewModel = LocalArrViewModel.current

    var isMonitored by remember { mutableStateOf(false) }

    val scrollState = rememberScrollState()

    Scaffold { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues.copy(bottom = 0.dp, top = 0.dp))
                .fillMaxSize()
        ) {
            arrViewModel?.let { arrViewModel ->
                val detailUiState by arrViewModel.detailsUiState.collectAsState()
                var isRefreshing by remember { mutableStateOf(true) }

                LaunchedEffect(isRefreshing) {
                    arrViewModel.getDetails(id)
                    isRefreshing = false
                }

                when (val state = detailUiState) {
                    is DetailsUiState.Initial,
                    is DetailsUiState.Loading -> {
                        LoadingIndicator(
                            modifier = Modifier
                                .size(96.dp)
                                .align(Alignment.Center)
                        )
                    }
                    is DetailsUiState.Error -> {
                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { isRefreshing = true }
                        ) {
                            Text(text = state.error.message)
                        }
                    }
                    is DetailsUiState.Success -> {
                        val item = state.item

                        SideEffect {
                            isMonitored = item.monitored
                        }

                        PullToRefreshBox(
                            isRefreshing = isRefreshing,
                            onRefresh = { isRefreshing = true }
                        ) {
                            Column(
                                modifier = Modifier.verticalScroll(scrollState),
                                verticalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                DetailsHeader(item)

                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp),
                                    verticalArrangement = Arrangement.spacedBy(24.dp)
                                ) {
                                    UpcomingDateView(item)

                                    item.overview?.let { overview ->
                                        ItemDescriptionCard(overview)
                                    }

                                    when (item) {
                                        is ArrSeries -> SeasonsArea(item)
                                        is ArrMovie -> MovieFileView(item)
                                    }

                                    InfoArea(item)
                                }

                                Spacer(modifier = Modifier.height(12.dp))
                            }
                        }
                    }
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
                            arrViewModel?.setMonitorStatus(id, !isMonitored)
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