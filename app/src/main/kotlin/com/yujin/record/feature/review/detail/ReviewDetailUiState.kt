package com.yujin.record.feature.review.detail

import com.yujin.record.core.model.ImageItem
import com.yujin.record.core.model.Review
import com.yujin.record.core.model.ReviewType
import kotlin.time.Clock
import kotlin.time.Instant

data class ReviewDetailUiState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val shortReview: String = "",
    val type: ReviewType = ReviewType.MOVIE,
    val creatorName: String = "",
    val rating: Float = 3f,
    val images: List<ImageItem> = emptyList(),
    val tags: List<String> = emptyList(),
    val selectedBookmarkId: Long = 0,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
)

fun Review.toUiState() = ReviewDetailUiState(
    id = id,
    title = title,
    content = content,
    shortReview = shortReview,
    type = reviewType,
    creatorName = creatorName,
    rating = rating,
    images = images,
    tags = tags,
    selectedBookmarkId = bookmarkId,
    createdAt = createdAt,
    updatedAt = updatedAt
)