package com.yujin.record.domain.model

data class Video(
    val id: Int,
    val type: ReviewType,
    val title: String,
    val posterPath: String = "",
    val creator: String = "",
)