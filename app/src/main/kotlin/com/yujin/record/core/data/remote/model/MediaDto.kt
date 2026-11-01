package com.yujin.record.core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MediaDto(
    val id: Int,
    val name: String? = null,
    val title: String? = null,
    @SerialName("poster_path")
    val posterPath: String?,
    @SerialName("media_type")
    val mediaType: TmdbMediaType = TmdbMediaType.UNKNOWN,
    @SerialName("release_date")
    val releaseDate: String? = null,
)