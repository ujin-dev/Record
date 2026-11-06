package com.yujin.record.model

data class Media(
    val id: Int,
    val displayName: String,
    val posterPath: String?,
    val mediaType: ReviewType?,
    val releaseDate: String? = null,
)