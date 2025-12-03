package com.dnfapps.arrmatey.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dnfapps.arrmatey.api.arr.viewmodel.RadarrViewModel
import com.dnfapps.arrmatey.model.Instance

@Composable
fun rememberRadarrViewModel(instance: Instance): RadarrViewModel {
    return remember { RadarrViewModel(instance) }
}