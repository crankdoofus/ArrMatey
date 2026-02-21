package com.dnfapps.arrmatey.instances.usecase

import com.dnfapps.arrmatey.arr.api.client.GenericClient

class TestNewInstanceConnectionUseCase(
    private val client: GenericClient
) {
    suspend operator fun invoke(url: String, apiKey: String): Boolean =
        client.test(url.trim(), apiKey.trim())
}