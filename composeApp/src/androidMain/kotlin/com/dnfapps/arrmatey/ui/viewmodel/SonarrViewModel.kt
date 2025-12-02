package com.dnfapps.arrmatey.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dnfapps.arrmatey.api.arr.SonarrClient
import com.dnfapps.arrmatey.api.arr.model.ArrSeries
import com.dnfapps.arrmatey.database.dao.SeriesDao
import com.dnfapps.arrmatey.model.Instance
import com.dnfapps.arrmatey.model.InstanceType
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class SonarrViewModel(instance: Instance): BaseArrViewModel<ArrSeries>(instance) {

    override val client: SonarrClient by inject { parametersOf(instance) }
    override val dao: SeriesDao by inject()

    init {
        if (instance.type != InstanceType.Sonarr) {
            throw IllegalArgumentException("Cannot instantiate SonarrViewModel with an instance of type ${instance.type}")
        }
    }

}

@Composable
fun rememberSonarrViewModel(instance: Instance): SonarrViewModel {
    return remember { SonarrViewModel(instance) }
}