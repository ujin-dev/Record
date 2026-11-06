package com.yujin.record.model

data class MediaItem(
    val id: Int,
    val type: ReviewType,
    val title: String,
    val posterPath: String? = ""
)