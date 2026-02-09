package com.dnfapps.arrmatey.compose.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.ComposeUIViewController
import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.ui.theme.SonarrDownloading

fun GridPosterItemViewController(
    item: ArrMedia,
    isActive: Boolean
) = ComposeUIViewController {
    Box(modifier = Modifier.fillMaxSize()) {
        PosterItem(
            item = item,
            onItemClick = {},
            modifier = Modifier.fillMaxSize(),
            additionalContent = {
                if (item.id != null) {
                    LinearProgressIndicator(
                        progress = { item.statusProgress },
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .height(6.dp),
                        color = if (isActive) SonarrDownloading else item.statusColor
                    )
                }
            }
        )
    }
}