package com.dnfapps.arrmatey.arr.api.model

import kotlinx.serialization.SerialName

enum class SeriesType {
    @SerialName("standard")
    Standard,

    @SerialName("daily")
    Daily,

    @SerialName("anime")
    Anime;

    companion object {
        fun allEntries() = entries.toList()
    }
}