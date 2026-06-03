package com.yujin.record.core.model

import com.yujin.record.feature.review.edit.ImageItem

data class Review(
    val id: Long,
    val title: String,
    val creatorName: String,
    val shortReview: String,
    val content: String,
    val reviewType: ReviewType,
    val images: List<ImageItem>,
    val tags: List<String>,
    val rating: Float,
)