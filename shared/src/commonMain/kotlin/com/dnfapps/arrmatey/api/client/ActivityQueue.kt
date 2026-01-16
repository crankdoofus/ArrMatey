package com.dnfapps.arrmatey.api.client

import com.dnfapps.arrmatey.api.arr.model.ArrMedia
import com.dnfapps.arrmatey.api.arr.model.CommandPayload
import com.dnfapps.arrmatey.api.arr.model.ArrRelease
import com.dnfapps.arrmatey.api.arr.model.QueueItem
import com.dnfapps.arrmatey.api.arr.model.ReleaseParams
import com.dnfapps.arrmatey.api.arr.viewmodel.BaseArrRepository
import com.dnfapps.arrmatey.api.arr.viewmodel.ArrRepository
import com.dnfapps.arrmatey.database.InstanceRepository
import com.dnfapps.arrmatey.model.Instance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import org.koin.core.component.inject
import org.koin.core.parameter.parametersOf

object ActivityQueue: KoinComponent {

    private val instanceRepository: InstanceRepository by inject()
    private val scope = CoroutineScope(Dispatchers.IO)

    private val _items = MutableStateFlow<Map<Long, List<QueueItem>>>(emptyMap())
    val items: StateFlow<Map<Long, List<QueueItem>>> = _items.asStateFlow()

    private val _itemsWithIssues = MutableStateFlow(0)
    val itemsWithIssues: StateFlow<Int> = _itemsWithIssues

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val instances = mutableMapOf<Instance, ArrRepository>()

    var shouldRefresh: Boolean = false

    private var timerJob: Job? = null

    init {
        startTimer()

        scope.launch {
            instanceRepository.allInstancesFlow.collect { allInstances ->
                if (allInstances.size == instances.size) return@collect
                instances.clear()
                instances.putAll(
                    allInstances.map { it to createRepository(it) }
                )
            }
        }
    }

    private fun createRepository(instance: Instance): BaseArrRepository {
        return get<BaseArrRepository> { parametersOf(instance) }
    }

    private fun startTimer() {
        timerJob?.cancel()
        timerJob = scope.launch {
            while(isActive) {
                 fetchActivity()
                if (shouldRefresh) {
                     fetchDownloads()
                }
                delay(5_000L)
            }
        }
    }

    suspend fun fetchActivity() {
        if (_isLoading.value) return
        _isLoading.value = true

        val newItems = mutableMapOf<Long, List<QueueItem>>()
        instances.forEach { (instance, repository) ->
            val pageResult = repository.fetchActivityTasksSync(instanceId = instance.id)
            when (pageResult) {
                is NetworkResult.Success -> newItems[instance.id] = pageResult.data.records
                else -> {}
            }
        }
        _items.value = newItems

        val issues = newItems.flatMap { it.value }.filter { it.hasIssue }
        val uniqueIssues = issues.map { it.taskGroup }.toSet().size

        if (_itemsWithIssues.value != uniqueIssues) {
            _itemsWithIssues.value = uniqueIssues
        }

        _isLoading.value = false
    }

    suspend fun fetchDownloads() {
        instances.map { (_, repository) ->
            repository.command(CommandPayload.RefreshMonitoredDownloads)
        }
    }

}