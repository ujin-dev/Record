package com.yujin.record.core.data.remote.model

import com.google.gson.annotations.SerializedName

data class MovieDetailResponse(
    val id: Int,
    val title: String,
    @SerializedName("poster_path")
    val posterPath: String? = null,
    val genres: List<GenreDto>,
    val credits: CreditsDto,
)

data class CreditsDto(
    val crew: List<CrewDto>,
    val cast: List<CastDto>,
)

data class CrewDto(
    val name: String,
    val job: String,
    val department: String,
)

data class CastDto(
    val name: String,
    val character: String,
    @SerializedName("known_for_department")
    val knownForDepartment: String,
)