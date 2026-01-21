package com.dnfapps.arrmatey.instances.usecase

import com.dnfapps.arrmatey.datastore.InstancePreferenceStoreRepository
import com.dnfapps.arrmatey.datastore.InstancePreferences

class UpdatePreferencesUseCase(
    private val instancePreferencesStoreRepository: InstancePreferenceStoreRepository
) {
    suspend fun savePreferences(instanceId: Long, preferences: InstancePreferences) {
        val preferenceStore = instancePreferencesStoreRepository.getInstancePreferences(instanceId)
        preferenceStore.savePreferences(preferences)
    }
}