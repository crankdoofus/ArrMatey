package com.dnfapps.arrmatey.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dnfapps.arrmatey.api.arr.viewmodel.SonarrViewModel
import com.dnfapps.arrmatey.model.Instance

@Composable
fun rememberSonarrViewModel(instance: Instance): SonarrViewModel {
    return remember { SonarrViewModel(instance) }
}