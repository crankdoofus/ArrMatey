package com.dnfapps.arrmatey.api.arr.model

import androidx.compose.ui.graphics.Color
import androidx.room.Ignore
import com.dnfapps.arrmatey.extensions.formatAsRuntime
import com.dnfapps.arrmatey.model.InstanceType
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.serialization.Contextual
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonDecoder
import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonEncoder
import kotlinx.serialization.json.JsonTransformingSerializer
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.encodeToJsonElement
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import kotlinx.serialization.json.put
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import kotlin.time.Instant

@Serializable
sealed interface AnyArrMedia: KoinComponent {
    companion object: KoinComponent {
        val json: Json by inject()
        fun fromJson(value: String): AnyArrMedia {
            return json.decodeFromString(AnyArrMediaSerializer, value)
        }
    }
    val id: Int?
    val title: String
    val originalLanguage: Language
    val year: Int
    val qualityProfileId: Int
    val monitored: Boolean
    val runtime: Int
    val tmdbId: Int
    val images: List<ArrImage>
    val sortTitle: String?
    val overview: String?
    val path: String?
    val cleanTitle: String?
    val imdbId: String?
    val titleSlug: String?
    val rootFolderPath: String?
    val folder: String?
    val certification: String?
    val genres: List<String>
    val tags: List<Int>
    val statistics: ArrStatistics?
    @Contextual val added: Instant
    fun ratingScore(): Double
    val statusProgress: Float
    val statusColor: Color
    val releasedBy: String?
    val statusString: String
    val fileSize: Long
    val runtimeString: String
    val infoItems: Flow<List<Info>>

    fun getPoster(): ArrImage?  {
        return images.firstOrNull { it.coverType == CoverType.Poster }
    }
    fun getBanner(): ArrImage? {
        return images.firstOrNull { it.coverType == CoverType.FanArt }
            ?: images.firstOrNull { it.coverType == CoverType.Banner }
            ?: images.firstOrNull { it.coverType == CoverType.Poster }
    }
}

@Serializable
class Info(val label: String, val value: String)

@Serializable
sealed class ArrMedia<AT, AO, R, STAT: ArrStatistics, S>: AnyArrMedia {
    abstract override val id: Int?
    abstract override val title: String
    abstract override val originalLanguage: Language
    abstract override val year: Int
    abstract override val qualityProfileId: Int
    abstract override val monitored: Boolean
    abstract override val runtime: Int
    abstract override val tmdbId: Int
    abstract val status: S
    abstract override val images: List<ArrImage>
    abstract override val sortTitle: String?
    abstract override val overview: String?
    abstract override val path: String?
    abstract override val cleanTitle: String?
    abstract override val imdbId: String?
    abstract override val titleSlug: String?
    abstract override val rootFolderPath: String?
    abstract override val folder: String?
    abstract override val certification: String?
    abstract override val genres: List<String>
    abstract override val tags: List<Int>
    abstract val alternateTitles: List<AT>
    abstract val addOptions: AO?
    abstract val ratings: R
    abstract override val statistics: STAT?
    @Contextual
    abstract override val added: Instant

    abstract override fun ratingScore(): Double

    abstract override val statusProgress: Float
    abstract override val statusColor: Color
    abstract override val releasedBy: String?
    abstract override val statusString: String

    override val fileSize: Long
        get() = statistics?.sizeOnDisk ?: 0L

    override val runtimeString: String
        get() = runtime.formatAsRuntime()

    abstract fun setMonitored(monitored: Boolean): ArrMedia<AT, AO, R, STAT, S>

    @Transient
    protected val _infoItems = MutableStateFlow<List<Info>>(emptyList())
    abstract override val infoItems: Flow<List<Info>>
}

fun AnyArrMedia.toJson(): String {
    val element: JsonElement = when (this) {
        is ArrSeries -> AnyArrMedia.json.encodeToJsonElement(ArrSeriesSerializer, this)
        is ArrMovie  -> AnyArrMedia.json.encodeToJsonElement(ArrMovieSerializer, this)
    }

    return AnyArrMedia.json.encodeToString(element)
}

object ArrSeriesSerializer :
    JsonTransformingSerializer<ArrSeries>(ArrSeries.serializer()) {
    override fun transformSerialize(element: JsonElement): JsonElement {
        val obj = element.jsonObject
        return buildJsonObject {
            obj.forEach { (k, v) -> put(k, v) }
            put("mediaType", InstanceType.Sonarr.name)
        }
    }
}

object ArrMovieSerializer :
    JsonTransformingSerializer<ArrMovie>(ArrMovie.serializer()) {
    override fun transformSerialize(element: JsonElement): JsonElement {
        val obj = element.jsonObject
        return buildJsonObject {
            obj.forEach { (k, v) -> put(k, v) }
            put("mediaType", InstanceType.Radarr.name)
        }
    }
}


object AnyArrMediaSerializer: KSerializer<AnyArrMedia> {
    override val descriptor: SerialDescriptor
        get() = buildClassSerialDescriptor("AnyArrmedia")

    override fun deserialize(decoder: Decoder): AnyArrMedia {
        require(decoder is JsonDecoder)
        val element = decoder.decodeJsonElement()
        val obj = element.jsonObject

        val mediaType = obj["mediaType"]?.jsonPrimitive?.content ?: error("mediaType is missing")

        return when (mediaType) {
            InstanceType.Sonarr.name -> decoder.json.decodeFromJsonElement(ArrSeries.serializer(), element)
            InstanceType.Radarr.name -> decoder.json.decodeFromJsonElement(ArrMovie.serializer(), element)
            else -> error("Unknown mediaType: $mediaType")
        }
    }

    override fun serialize(encoder: Encoder, value: AnyArrMedia) {
        require(encoder is JsonEncoder)
        val json = encoder.json
        val element: JsonElement = when (value) {
            is ArrSeries -> json.encodeToJsonElement(ArrSeriesSerializer, value)
            is ArrMovie  -> json.encodeToJsonElement(ArrMovieSerializer, value)
        }
        encoder.encodeJsonElement(element)
    }
}