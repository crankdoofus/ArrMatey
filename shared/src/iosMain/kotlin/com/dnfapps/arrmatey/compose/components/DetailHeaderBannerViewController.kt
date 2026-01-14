package com.dnfapps.arrmatey.compose.components

import androidx.compose.ui.window.ComposeUIViewController
import com.dnfapps.arrmatey.api.arr.model.AnyArrMedia
import com.dnfapps.arrmatey.ui.theme.BasicTheme

fun <T: AnyArrMedia> DetailHeaderBannerViewController(
    item: T
) = ComposeUIViewController {
    BasicTheme {
        DetailHeaderBanner(item.getBanner()?.remoteUrl)
    }
}