package com.dnfapps.arrmatey.compose.utils

import com.dnfapps.arrmatey.arr.api.model.QueueItem
import com.dnfapps.arrmatey.shared.MR
import dev.icerock.moko.resources.StringResource

enum class QueueSortBy(val resource: StringResource) {
    Title(MR.strings.title),
    Added(MR.strings.added);

    companion object {
        fun allEntries() = entries.toList()
    }
}

fun List<QueueItem>.applySorting(sortBy: QueueSortBy, sortOrder: SortOrder) = when(sortBy) {
    QueueSortBy.Title -> sortedBy { it.titleLabel }
    QueueSortBy.Added -> sortedBy { it.added }
}.let { when(sortOrder) {
    SortOrder.Asc -> it
    SortOrder.Desc -> it.reversed()
} }

fun List<QueueItem>.filterBy(instanceId: Long?) = when(instanceId) {
    null -> this
    else -> filter { item -> item.instanceId == instanceId }
}