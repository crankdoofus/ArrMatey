package com.dnfapps.arrmatey.ui.components

import android.graphics.BlurMaskFilter
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.dnfapps.arrmatey.arr.api.model.ArrMedia
import com.dnfapps.arrmatey.compose.utils.rememberRemoteUrlData
import com.dnfapps.arrmatey.entensions.Bullet

@Composable
fun DetailsHeader(item: ArrMedia) {
    Box(
        modifier = Modifier.fillMaxWidth()
    ) {
        DetailHeaderBanner(item.getBanner()?.remoteUrl)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 170.dp)
                .padding(horizontal = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            PosterItem(
                item = item,
                modifier = Modifier.height(220.dp)
            )
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                item.getClearLogo()?.remoteUrl?.let { logo ->
                    val isLightTheme = !isSystemInDarkTheme()
                    Box(
                        modifier = Modifier
                            .wrapContentSize()
                            .drawBehind {
                                if (isLightTheme) {
                                    drawIntoCanvas { canvas ->
                                        val paint = Paint().apply {
                                            color = Color.Black.copy(alpha = 0.4f)
                                            asFrameworkPaint().maskFilter =
                                                BlurMaskFilter(10f, BlurMaskFilter.Blur.NORMAL)
                                        }
                                        canvas.drawRoundRect(
                                            left = 0f,
                                            top = 0f,
                                            right = size.width,
                                            bottom = size.height,
                                            radiusX = 16.dp.toPx(),
                                            radiusY = 16.dp.toPx(),
                                            paint = paint
                                        )
                                    }
                                }
                            }
                    ) {
                        AsyncImage(
                            model = rememberRemoteUrlData(logo),
                            contentDescription = item.title,
                            modifier = Modifier
                                .heightIn(min = 64.dp)
                        )
                    }
                } ?: run {
                    Text(
                        text = item.title,
                        fontSize = 38.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 42.sp,
                        maxLines = 3,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Text(
                    text = listOf(
                        item.year,
                        item.runtimeString,
                        item.certification
                    ).joinToString(Bullet),
                    fontSize = 16.sp
                )
                Text(
                    text = listOf(item.releasedBy, item.statusString).joinToString(Bullet),
                    fontSize = 14.sp,
                    lineHeight = 16.sp
                )
                Text(
                    text = item.genres.joinToString(Bullet),
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary,
                    overflow = TextOverflow.Ellipsis,
                    lineHeight = 16.sp
                )
            }
        }
    }
}