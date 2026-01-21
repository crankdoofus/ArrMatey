package com.dnfapps.arrmatey.di

import com.dnfapps.arrmatey.arr.service.ActivityQueueService
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

fun initKoin() = startKoin {
    printLogger(Level.ERROR)
    modules(appModules())
}.also {
    it.koin.get<ActivityQueueService>().startPolling()
}