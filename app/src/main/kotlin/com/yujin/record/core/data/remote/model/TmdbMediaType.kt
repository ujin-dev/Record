package com.yujin.record.core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TmdbMediaType {
    @SerialName("movie")
    MOVIE,

    @SerialName("tv")
    TV,

    UNKNOWN
}