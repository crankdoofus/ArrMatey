package com.dnfapps.arrmatey.arr.api.model

import kotlinx.serialization.Serializable

@Serializable
data class Quality(
    val id: Int,
    val name: String,
    val source: String,
    val resolution: Int,
    val modifier: String? = null
)
