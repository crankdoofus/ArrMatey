package com.dnfapps.arrmatey.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import com.dnfapps.arrmatey.instances.model.InstanceType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class PreferencesStore(
    dataStoreFactory: DataStoreFactory
) {
    private val dataStore: DataStore<Preferences> = dataStoreFactory.provideDataStore()

    private val SONARR_INFO_CARD_KEY = booleanPreferencesKey("sonarrInfoCard")
    private val RADARR_INFO_CARD_KEY = booleanPreferencesKey("radarrInfoCard")
    private val activityPollingKey = booleanPreferencesKey("enableActivityPolling")

    private fun infoCardKey(type: InstanceType): Preferences.Key<Boolean> = when (type) {
        InstanceType.Sonarr -> SONARR_INFO_CARD_KEY
        InstanceType.Radarr -> RADARR_INFO_CARD_KEY
    }

    private val scope = CoroutineScope(Dispatchers.IO)

    val showInfoCards: Flow<Map<InstanceType, Boolean>> = dataStore.data
        .map { preferences ->
            InstanceType.entries.associateWith { type -> (preferences[infoCardKey(type)] ?: true) }
        }

    fun dismissInfoCard(type: InstanceType) {
        setInfoCardVisibility(type, false)
    }

    fun setInfoCardVisibility(type: InstanceType, value: Boolean) {
        scope.launch {
            dataStore.edit { preferences ->
                preferences[infoCardKey(type)] = value
            }
        }
    }

    private var _isPollingEnabled: Boolean = true
    val isPollingEnabled: Boolean
        get() = _isPollingEnabled

    val enableActivityPolling: Flow<Boolean> = dataStore.data
        .map { preferences ->
            val value = preferences[activityPollingKey] ?: true
            _isPollingEnabled = value
            value
        }

    fun toggleActivityPolling() {
        scope.launch {
            dataStore.edit { preferences ->
                val isPolling = preferences[activityPollingKey] ?: true
                preferences[activityPollingKey] = !isPolling
            }
        }
    }
}