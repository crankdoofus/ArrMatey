package com.dnfapps.arrmatey.api.arr.model

import kotlinx.serialization.Serializable

@Serializable
data class QualityInfo(
    val quality: Quality,
    val revision: Revision
) {
    val qualityLabel: String
        get() {
            val name = quality.name
            val resolution = quality.resolution

            if (name.contains(resolution.toString())) {
                return name
            }

            if (resolution > 0) {
                return "$name ${resolution}p"
            }

            return name
        }
}
