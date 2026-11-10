package com.yujin.record.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class MovieSearchResponse(
    val results: List<MediaDto>
)