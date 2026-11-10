package com.yujin.record.domain.model

data class TmdbMedia(
    val id: Int,
    val displayName: String,
    val posterPath: String?,
    val videoType: ReviewType?,
    val releaseDate: String? = null,
)