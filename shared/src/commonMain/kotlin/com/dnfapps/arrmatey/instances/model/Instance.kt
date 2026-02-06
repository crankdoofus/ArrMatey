package com.dnfapps.arrmatey.instances.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.dnfapps.arrmatey.shared.MR
import dev.icerock.moko.resources.StringResource

@Entity(
    tableName = "instances",
    indices = [
        Index(value = ["url"], unique = true),
        Index(value = ["label"], unique = true)
    ]
)
data class Instance(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: InstanceType,
    val label: String,
    val url: String,
    val apiKey: String,
    val enabled: Boolean = true,
    val slowInstance: Boolean = false,
    val customTimeout: Long? = null,
    val selected: Boolean = false
)

enum class InstanceType(
    val resource: StringResource,
    val iconKey: String,
    val github: String,
    val website: String,
    val defaultPort: Int,
    val supportsActivityQueue: Boolean
) {
    Sonarr(
        resource = MR.strings.sonarr_description,
        github = "https://github.com/Sonarr/Sonarr",
        website = "https://sonarr.tv/",
        iconKey = "sonarr",
        defaultPort = 8989,
        supportsActivityQueue = true
    ),
    Radarr(
        resource = MR.strings.radarr_description,
        github = "https://github.com/Radarr/Radarr",
        website = "https://radarr.video/",
        iconKey = "radarr",
        defaultPort = 7878,
        supportsActivityQueue = true
    );

    companion object {
        fun allValue() = entries.toList()
    }
}
