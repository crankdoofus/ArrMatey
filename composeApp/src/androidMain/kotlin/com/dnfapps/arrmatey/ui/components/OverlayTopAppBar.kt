package com.dnfapps.arrmatey.ui.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.dnfapps.arrmatey.ui.helpers.statusBarHeight

@Composable
fun OverlayTopAppBar(
    scrollState: ScrollState,
    modifier: Modifier = Modifier,
    navigationIcon: @Composable () -> Unit = {},
    title: @Composable () -> Unit = {},
    actions: @Composable RowScope.() -> Unit = {}
) {
    val headerBackgroundAlpha by remember {
        derivedStateOf {
            // Fade in over 200 pixels of scroll
            val fadeDistance = 200f
            (scrollState.value / fadeDistance).coerceIn(0f, 1f)
        }
    }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.surface.copy(
                    alpha = headerBackgroundAlpha
                )
            )
            .padding(top = statusBarHeight())
            .padding(horizontal = 6.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(6.dp)
    ) {
        navigationIcon()
        title()
        Spacer(modifier = Modifier.weight(1f))
        actions()
    }
}