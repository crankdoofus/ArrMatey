package com.dnfapps.arrmatey.ui.components.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.dnfapps.arrmatey.compose.TabItem
import com.dnfapps.arrmatey.navigation.NavigationManager
import org.koin.compose.koinInject

@Composable
fun NavigationDrawerButton(returnToHome: Boolean = false) {
    val navigationManager: NavigationManager = koinInject()
    IconButton(onClick = {
//        navigationManager.openDrawer()
        val isOpen = !returnToHome
        navigationManager.setDrawerOpen(isOpen)
        val tab = if (isOpen) TabItem.SETTINGS else null
        navigationManager.setSelectedDrawerTab(tab)
    } ) {
        if (returnToHome) {
            Icon(Icons.AutoMirrored.Default.ArrowBack, null)
        } else {
            Icon(Icons.Default.Settings, null)
        }
//        Icon(Icons.Default.Menu, null)
    }
}