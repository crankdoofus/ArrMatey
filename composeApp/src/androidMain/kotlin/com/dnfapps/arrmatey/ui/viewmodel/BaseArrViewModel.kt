package com.dnfapps.arrmatey.ui.viewmodel

import androidx.compose.runtime.Composable
import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import com.dnfapps.arrmatey.api.arr.viewmodel.BaseArrViewModel
import com.dnfapps.arrmatey.model.Instance
import com.dnfapps.arrmatey.model.InstanceType

@Composable
fun rememberArrViewModel(instance: Instance): BaseArrViewModel<out ArrMedia<*, *, *, *, *>> {
    return when (instance.type) {
        InstanceType.Sonarr -> rememberSonarrViewModel(instance)
        InstanceType.Radarr -> rememberRadarrViewModel(instance)
    }
}

