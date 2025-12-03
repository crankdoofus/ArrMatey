package com.dnfapps.arrmatey.database

import com.dnfapps.arrmatey.database.dao.InstanceDao
import com.dnfapps.arrmatey.model.Instance
import com.dnfapps.arrmatey.model.InstanceType
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class InstanceRepository: KoinComponent {

    private val instanceDao: InstanceDao by inject()

    val allInstances = instanceDao.getAllAsFlow()

    suspend fun newInstance(instance: Instance) {
        instanceDao.insert(instance)
    }

    fun getFirstInstance(instanceType: InstanceType) = instanceDao.getFirstInstance(instanceType)

}