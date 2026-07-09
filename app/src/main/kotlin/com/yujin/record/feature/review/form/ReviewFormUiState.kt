package com.yujin.record.feature.review.form

import com.yujin.record.core.model.ImageItem
import com.yujin.record.core.model.Review
import com.yujin.record.core.model.ReviewType
import kotlin.time.Clock
import kotlin.time.Instant

data class ReviewFormUiState(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val shortReview: String = "",
    val type: ReviewType = ReviewType.MOVIE,
    val creatorName: String = "",
    val rating: Float = 3f,
    val images: List<ImageItem> = emptyList(),
    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val selectedBookmarkId: Long = 0,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
)

fun ReviewFormUiState.toReview() = Review(
    id = id,
    title = title.trim(),
    content = content.trim(),
    shortReview = shortReview.trim(),
    reviewType = type,
    creatorName = creatorName.trim(),
    rating = rating,
    images = images,
    tags = tags,
    bookmarkId = selectedBookmarkId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)