package com.yujin.record.presentation.reviewdetail

import com.yujin.record.domain.model.ReviewImage
import com.yujin.record.domain.model.Review
import com.yujin.record.domain.model.ReviewType
import kotlin.time.Clock
import kotlin.time.Instant

data class ReviewDetailUiState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val type: ReviewType = ReviewType.MOVIE,
    val creatorName: String = "",
    val rating: Float = 3f,
    val images: List<ReviewImage> = emptyList(),
    val tags: List<String> = emptyList(),
    val selectedBookmarkId: Long = 0,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
)

fun Review.toUiState() = ReviewDetailUiState(
    id = id,
    title = title,
    content = content,
    type = reviewType,
    creatorName = creatorName,
    rating = rating,
    images = images,
    tags = tags,
    selectedBookmarkId = bookmarkId,
    createdAt = createdAt,
    updatedAt = updatedAt
)