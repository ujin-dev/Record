package com.yujin.record.core.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieDetailResponse(
    val id: Int,
    val title: String,
    @SerialName("poster_path")
    val posterPath: String? = null,
    val genres: List<GenreDto>,
    val credits: CreditsDto,
)

@Serializable
data class CreditsDto(
    val crew: List<CrewDto>,
    val cast: List<CastDto>,
)

@Serializable
data class CrewDto(
    val name: String,
    val job: String,
    val department: String,
)

@Serializable
data class CastDto(
    val name: String,
    val character: String,
    @SerialName("known_for_department")
    val knownForDepartment: String,
)