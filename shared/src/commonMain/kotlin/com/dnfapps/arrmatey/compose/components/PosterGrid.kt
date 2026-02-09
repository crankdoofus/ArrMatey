package com.dnfapps.arrmatey.compose.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BrokenImage
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.arr.api.model.Episode
import com.dnfapps.arrmatey.compose.utils.rememberRemoteUrlData
import com.dnfapps.arrmatey.ui.theme.SonarrDownloading

@Composable
fun <T: ArrMedia> PosterGrid(
    items: List<T>,
    onItemClick: (T) -> Unit,
    itemIsActive: (T) -> Boolean,
    userScrollEnabled: Boolean = true,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Adaptive(minSize = 120.dp),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        userScrollEnabled = userScrollEnabled
    ) {
        items(items) { item ->
            val isActive = itemIsActive(item)
            PosterItem(
                item = item,
                onItemClick = onItemClick,
                modifier = Modifier.padding(8.dp),
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
}

@Composable
fun <T: ArrMedia> PosterItem(
    item: T,
    modifier: Modifier = Modifier,
    onItemClick: ((T) -> Unit)? = null,
    enabled: Boolean = true,
    elevation: Dp = 8.dp,
    radius: Dp = 10.dp,
    additionalContent: @Composable BoxScope.() -> Unit = {}
) {
    var imageLoadError by remember { mutableStateOf(false) }
    var imageLoaded by remember { mutableStateOf(false) }

    val url = item.getPoster()?.remoteUrl

    val shadowModifier = if (elevation > 0.dp) {
        Modifier.shadow(
            elevation = elevation,
            shape = RoundedCornerShape(radius),
            clip = false
        )
    } else { Modifier }

    Box(
        modifier = modifier
            .clip(RoundedCornerShape(radius))
            .background(MaterialTheme.colorScheme.surface)
            .then(shadowModifier)
            .aspectRatio(0.675f, true)
            .clickable(
                enabled = enabled && onItemClick != null,
                onClick = {
                    onItemClick?.invoke(item)
                }
            )
    ) {
        AsyncImage(
            model = rememberRemoteUrlData(
                url = url,
                onError = { _, err ->
                    println(err.throwable.message)
                    imageLoadError = true
                },
                onSuccess = { _, _ -> imageLoaded = true }
            ),
            contentDescription = null,
            contentScale = ContentScale.FillBounds
        )
        if (imageLoadError) {
            Icon(
                imageVector = Icons.Default.BrokenImage,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(64.dp).align(Alignment.Center)
            )
        }
        if (imageLoaded) {
            additionalContent()
        }
    }
}

@Composable
fun EpisodePosterItem(episode: Episode) {
    val url = episode.getPoster()?.remoteUrl
    Box(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.surface)
            .height(100.dp)
    ) {
        AsyncImage(
            model = rememberRemoteUrlData(url),
            contentDescription = null,
            contentScale = ContentScale.FillHeight
        )
    }
}