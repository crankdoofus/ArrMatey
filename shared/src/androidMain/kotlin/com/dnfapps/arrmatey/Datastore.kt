package com.dnfapps.arrmatey

import android.content.Context
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class DataStoreFactory: KoinComponent {
    private val context: Context by inject()

    actual fun provideDataStore() = createDataStore(
        producePath = { context.filesDir.resolve(dataStoreFileName).absolutePath }
    )
}