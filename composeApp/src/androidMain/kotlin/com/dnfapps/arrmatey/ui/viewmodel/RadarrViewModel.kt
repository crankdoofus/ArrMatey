package com.dnfapps.arrmatey.ui.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import com.dnfapps.arrmatey.api.arr.RadarrClient
import com.dnfapps.arrmatey.api.arr.model.ArrMovie
import com.dnfapps.arrmatey.database.dao.MovieDao
import com.dnfapps.arrmatey.model.Instance
import com.dnfapps.arrmatey.model.InstanceType
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

class RadarrViewModel(instance: Instance): BaseArrViewModel<ArrMovie>(instance) {

    override val client: RadarrClient by inject { parametersOf(instance) }
    override val dao: MovieDao by inject()

    init {
        if (instance.type != InstanceType.Radarr) {
            throw IllegalArgumentException("Cannot instantiate RadarrViewModel with an instance of type ${instance.type}")
        }
    }

}

@Composable
fun rememberRadarrViewModel(instance: Instance): RadarrViewModel {
    return remember { RadarrViewModel(instance) }
}