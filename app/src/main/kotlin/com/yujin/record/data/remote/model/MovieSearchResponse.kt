package com.yujin.record.data.remote.model

import kotlinx.serialization.Serializable

@Serializable
data class MovieSearchResponse(
    val results: List<MediaDto>
)