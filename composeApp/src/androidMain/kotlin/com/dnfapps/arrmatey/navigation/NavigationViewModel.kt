package com.dnfapps.arrmatey.navigation

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import com.dnfapps.arrmatey.compose.TabItem

abstract class NavViewModel<T>(initialScreen: T): ViewModel() {
    val backStack = mutableStateListOf(initialScreen)

    fun navigateTo(screen: T) {
        backStack.add(screen)
    }

    fun popBackStack() {
        backStack.removeLastOrNull()
    }
}

class RootNavigation: NavViewModel<RootScreen>(RootScreen.HomeScreen)
class SettingsNavigation: NavViewModel<SettingsScreen>(SettingsScreen.Landing)

class HomeTabNavigation: NavViewModel<HomeTab>(HomeTab.SeriesTab) {
    fun navigateToHomeTab(tab: TabItem) {
        val navKey = when (tab) {
            TabItem.SHOWS -> HomeTab.SeriesTab
            TabItem.MOVIES -> HomeTab.MoviesTab
            TabItem.SETTINGS -> HomeTab.SettingsTab
        }
        backStack.add(navKey)
    }


}