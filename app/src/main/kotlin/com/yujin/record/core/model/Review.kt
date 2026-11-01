package com.yujin.record.core.model

import kotlin.time.Instant

data class Review(
    val id: Long,

    val title: String,
    val creatorName: String = "",
    val content: String = "",
    val reviewType: ReviewType,
    val rating: Float,

    val images: List<ImageItem> = emptyList(),
    val tags: List<String> = emptyList(),
    val bookmarkId: Long = 0,

    val createdAt: Instant,
    val updatedAt: Instant,
)