package com.dnfapps.arrmatey.ui.viewmodel

import com.dnfapps.arrmatey.api.arr.IArrClient
import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import com.dnfapps.arrmatey.database.dao.BaseArrDao
import com.dnfapps.arrmatey.model.Instance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent

abstract class BaseArrViewModel<T: ArrMedia<*,*,*,*,*>>(protected val instance: Instance): KoinComponent, IArrViewModel {

    abstract val client: IArrClient<T>
    abstract val dao: BaseArrDao<T>

    protected val _library = MutableStateFlow<List<T>>(emptyList())
    val library: StateFlow<List<T>> = _library

    init {
        if (instance.cacheOnDisk) {
            CoroutineScope(Dispatchers.IO).launch {
                val cached = dao.getAll(instance.id)
                _library.emit(cached)
            }
        }
    }

    override suspend fun refreshLibrary() {
        val newLibrary = client.getLibrary()
        _library.emit(newLibrary)
        if(instance.cacheOnDisk) {
            CoroutineScope(Dispatchers.IO).launch {
                newLibrary.map { s ->
                    s.apply { instanceId = instance.id }
                }.let {
                    dao.clearAll()
                    dao.insertAll(it)
                }
            }
        }
    }

}