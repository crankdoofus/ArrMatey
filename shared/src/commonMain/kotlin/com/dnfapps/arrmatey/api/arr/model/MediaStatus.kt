package com.dnfapps.arrmatey.api.arr.model

import kotlinx.serialization.SerialName

enum class MediaStatus {
    // Sonarr Statuses
    @SerialName("continuing")
    Continuing,
    @SerialName("ended")
    Ended,
    @SerialName("upcoming")
    Upcoming,

    // Radarr Statuses
    @SerialName("tba")
    Tba,
    @SerialName("announced")
    Announced,
    @SerialName("inCinemas")
    InCinemas,
    @SerialName("released")
    Released,

    // Shared
    @SerialName("deleted")
    Deleted;

    companion object {
        fun seriesValues() = listOf(Continuing, Ended, Upcoming, Deleted)
        fun movieValues() = listOf(Tba, Announced, InCinemas, Released, Deleted)
    }
}