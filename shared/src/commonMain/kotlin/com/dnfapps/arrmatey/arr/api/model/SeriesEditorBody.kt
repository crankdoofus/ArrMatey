package com.dnfapps.arrmatey.arr.api.model

import kotlinx.serialization.Serializable

@Serializable
data class SeriesEditorBody(
    val seriesIds: List<Long>,
    val monitored: Boolean,
    val monitorNewItems: SeriesMonitorNewItems,
    val seriesType: SeriesType,
    val seasonFolder: Boolean,
    val qualityProfileId: Int,
    val rootFolderPath: String?,
    val tags: List<Int>,
    val applyTags: String,
    val moveFiles: Boolean
)