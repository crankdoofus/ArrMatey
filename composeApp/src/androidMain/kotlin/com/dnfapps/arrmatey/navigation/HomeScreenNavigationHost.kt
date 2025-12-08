package com.dnfapps.arrmatey.navigation

import androidx.activity.compose.LocalActivity
import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.dnfapps.arrmatey.model.InstanceType
import com.dnfapps.arrmatey.ui.tabs.ArrTab

@Composable
fun HomeScreenNavHost() {
    val navigationViewModel = viewModel<HomeTabNavigation>()
    val activity = LocalActivity.current

    NavDisplay(
        backStack = navigationViewModel.backStack,
        onBack = {
            activity?.finish() ?: run {
                navigationViewModel.popBackStack()
            }
        },
        entryProvider = entryProvider {
            entry<HomeTab.SeriesTab> { ArrTab(InstanceType.Sonarr) }
            entry<HomeTab.MoviesTab> { ArrTab(InstanceType.Radarr) }
            entry<HomeTab.SettingsTab> { SettingsTabNavHost() }
        }
    )
}