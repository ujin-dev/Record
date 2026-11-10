package com.yujin.record.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
enum class TmdbMediaType {
    @SerialName("movie")
    MOVIE,

    @SerialName("tv")
    TV
}
