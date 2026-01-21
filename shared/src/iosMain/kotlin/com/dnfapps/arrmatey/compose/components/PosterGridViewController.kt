package com.dnfapps.arrmatey.compose.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.dnfapps.arrmatey.arr.api.model.ArrMedia

fun PosterGridViewController(
    items: List<ArrMedia>,
    itemIsActive: (ArrMedia) -> Boolean,
    onItemClick: (ArrMedia) -> Unit = {},
) = ComposeUIViewController {
    PosterGrid(
        items = items,
        onItemClick = onItemClick,
        itemIsActive = itemIsActive,
        modifier = Modifier.fillMaxSize()
    )
}