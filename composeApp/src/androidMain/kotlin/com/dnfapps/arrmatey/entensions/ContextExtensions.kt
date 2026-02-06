package com.dnfapps.arrmatey.entensions

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

fun Context.getDrawableId(key: String): Int {
    return resources.getIdentifier(key, "drawable", packageName)
}

@Composable
fun getDrawableId(key: String): Int {
    val context = LocalContext.current
    return context.getDrawableId(key)
}