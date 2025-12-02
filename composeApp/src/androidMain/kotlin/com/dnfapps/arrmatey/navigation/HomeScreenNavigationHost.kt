package com.dnfapps.arrmatey.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.ui.NavDisplay
import com.dnfapps.arrmatey.model.InstanceType
import com.dnfapps.arrmatey.ui.tabs.ArrTab

@Composable
fun HomeScreenNavHost() {
    val navigationViewModel = viewModel<NavigationViewModel>()
    val backStack = remember { navigationViewModel.homeTabBackStack }

    val activity = LocalActivity.current

    NavDisplay(
        backStack = backStack,
        onBack = {
            activity?.finish() ?: run {
                backStack.removeLastOrNull()
            }
        },
        entryProvider = { key ->
            when (key) {
                HomeTab.SeriesTab -> NavEntry(key) { ArrTab(InstanceType.Sonarr) }
                HomeTab.MoviesTab -> NavEntry(key) { ArrTab(InstanceType.Radarr) }
                HomeTab.SettingsTab -> NavEntry(key) { SettingsTabNavHost() }
            }
        }
    )
}