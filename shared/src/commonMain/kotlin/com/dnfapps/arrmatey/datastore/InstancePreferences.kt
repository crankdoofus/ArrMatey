package com.dnfapps.arrmatey.datastore

import com.dnfapps.arrmatey.compose.utils.FilterBy
import com.dnfapps.arrmatey.compose.utils.SortBy
import com.dnfapps.arrmatey.compose.utils.SortOrder
import com.dnfapps.arrmatey.ui.theme.ViewType

data class InstancePreferences(
    val sortBy: SortBy = SortBy.Title,
    val sortOrder: SortOrder = SortOrder.Asc,
    val filterBy: FilterBy = FilterBy.All,
    val viewType: ViewType = ViewType.Grid
) {
    companion object {
        fun empty() = InstancePreferences()
    }
}