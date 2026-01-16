package com.dnfapps.arrmatey.compose.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import platform.UIKit.UIColor

fun <T: ArrMedia> PosterItemViewController(
    item: T,
    onItemClick: ((T) -> Unit)? = null,
    enabled: Boolean = true
) = ComposeUIViewController {
    PosterItem(
        item = item,
        onItemClick = onItemClick,
        enabled = enabled,
        elevation = 0.dp,
        radius = 0.dp,
        modifier = Modifier
            .fillMaxSize()
    )
}.apply {
    view.backgroundColor = UIColor.clearColor
}