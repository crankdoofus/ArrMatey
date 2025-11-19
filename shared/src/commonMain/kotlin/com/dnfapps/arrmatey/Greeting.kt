package com.dnfapps.arrmatey

import com.dnfapps.arrmatey.ktor.demo.RocketComponent
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class Greeting : KoinComponent {
    private val rocketComponent: RocketComponent by inject()

    fun greet(): Flow<String> = flow {
        emit(rocketComponent.launchPhrase())
    }
}