package com.dnfapps.arrmatey.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.ComposeUIViewController
import com.dnfapps.arrmatey.arr.api.model.ArrMedia

fun MediaListItemViewController(
    item: ArrMedia,
    isActive: Boolean
) = ComposeUIViewController {
    Box(modifier = Modifier.fillMaxSize()) {
        MediaItem(item, {}, isActive)
    }
}