package com.dnfapps.arrmatey.database

import com.dnfapps.arrmatey.database.dao.InstanceDao
import com.dnfapps.arrmatey.model.Instance
import com.dnfapps.arrmatey.model.InstanceType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InstanceRepository: KoinComponent {

    private val instanceDao: InstanceDao by inject()

    val allInstances = instanceDao.observeAllInstances()
        .stateIn(
            scope = CoroutineScope(Dispatchers.IO),
            started = SharingStarted.Eagerly,
            initialValue = emptyList()
        )

    suspend fun newInstance(instance: Instance) {
        val shouldBeSelected = allInstances.value
                .none { i ->
                    i.type == instance.type && i.selected
                }
        instance.selected = shouldBeSelected
        instanceDao.insert(instance)
    }

    suspend fun setInstanceActive(instance: Instance) {
        instanceDao.setInstanceAsSelected(instance.id, instance.type)
    }

}