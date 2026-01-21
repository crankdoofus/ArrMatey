package com.dnfapps.arrmatey.compose.components

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.dnfapps.arrmatey.arr.api.model.ArrMedia

fun MediaListViewController(
    items: List<ArrMedia>,
    onItemClick: (ArrMedia) -> Unit = {},
    itemIsActive: (ArrMedia) -> Boolean
) = ComposeUIViewController {
    MediaList(
        items = items,
        onItemClick = onItemClick,
        itemIsActive = itemIsActive,
        modifier = Modifier
            .padding(horizontal = 12.dp)
            .fillMaxSize()
    )
}