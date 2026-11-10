package com.yujin.record.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TvSeriesDetailResponse(
    val id: Int,
    val name: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    val genres: List<GenreDto>,
    @SerialName("created_by")
    val createdBy: List<CreatedByDto>,
)

@Serializable
data class CreatedByDto(
    val name: String
)
