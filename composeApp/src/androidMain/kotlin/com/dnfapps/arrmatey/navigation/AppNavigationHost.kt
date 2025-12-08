package com.dnfapps.arrmatey.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.ui.NavDisplay
import com.dnfapps.arrmatey.ui.screens.HomeScreen

@Composable
fun AppNavHost() {
    val navigationViewModel = viewModel<RootNavigation>()

    NavDisplay(
        backStack = navigationViewModel.backStack,
        onBack = { navigationViewModel.popBackStack() },
        entryProvider = entryProvider {
            entry<RootScreen.HomeScreen> { HomeScreen() }
        }
    )
}