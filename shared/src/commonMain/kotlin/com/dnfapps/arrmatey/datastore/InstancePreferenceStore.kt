package com.dnfapps.arrmatey.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.dnfapps.arrmatey.compose.utils.FilterBy
import com.dnfapps.arrmatey.compose.utils.SortBy
import com.dnfapps.arrmatey.compose.utils.SortOrder
import com.dnfapps.arrmatey.ui.theme.ViewType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map

class InstancePreferenceStore(
    instanceId: Long,
    dataStoreFactory: DataStoreFactory
) {
    private val dataStore: DataStore<Preferences> =
        dataStoreFactory.provideInstanceDataStore(instanceId)

    private val viewTypeKey = stringPreferencesKey("viewType")
    private val sortByKey = stringPreferencesKey("sortBy")
    private val sortOrderKey = stringPreferencesKey("sortOrder")
    private val filterByKey = stringPreferencesKey("filterBy")

    private val sortByFlow: Flow<SortBy> = dataStore.data
        .map { preferences ->
            preferences[sortByKey]?.let { SortBy.valueOf(it) } ?: SortBy.Title
        }

    private val sortOrderFlow: Flow<SortOrder> = dataStore.data
        .map { preferences ->
            preferences[sortOrderKey]?.let { SortOrder.valueOf(it) } ?: SortOrder.Asc
        }

    private val filterByFlow: Flow<FilterBy> = dataStore.data
        .map { preferences ->
            preferences[filterByKey]?.let { FilterBy.valueOf(it) } ?: FilterBy.All
        }

    private val viewTypeFlow: Flow<ViewType> = dataStore.data
        .map { preferences ->
            preferences[viewTypeKey]?.let { ViewType.valueOf(it) } ?: ViewType.Grid
        }

    fun observePreferences(): Flow<InstancePreferences> = combine(
        sortByFlow, sortOrderFlow, filterByFlow, viewTypeFlow
    ) { sortBy, sortOrder, filterBy, viewType ->
        InstancePreferences(sortBy, sortOrder, filterBy, viewType)
    }

    suspend fun savePreferences(preferences: InstancePreferences) {
        dataStore.edit { prefs ->
            prefs[sortByKey] = preferences.sortBy.name
            prefs[sortOrderKey] = preferences.sortOrder.name
            prefs[filterByKey] = preferences.filterBy.name
            prefs[viewTypeKey] = preferences.viewType.name
        }
    }
}