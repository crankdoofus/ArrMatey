package com.dnfapps.arrmatey.arr.api.model

import kotlinx.serialization.SerialName

enum class CoverType {
    @SerialName("clearlogo")
    ClearLogo,

    @SerialName("banner")
    Banner,

    @SerialName("poster")
    Poster,

    @SerialName("fanart")
    FanArt,

    @SerialName("screenshot")
    Screenshot,

    Undefined
}