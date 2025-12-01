package com.dnfapps.arrmatey.api.arr.model

import kotlinx.serialization.SerialName

enum class SeriesStatus {
    @SerialName("continuing")
    Continuing,
    @SerialName("ended")
    Ended,
    @SerialName("upcoming")
    Upcoming,

    @SerialName("deleted")
    Deleted
}