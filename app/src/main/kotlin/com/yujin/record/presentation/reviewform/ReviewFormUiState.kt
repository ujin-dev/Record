package com.yujin.record.presentation.reviewform

import com.yujin.record.domain.model.ReviewImage
import com.yujin.record.domain.model.Video
import com.yujin.record.domain.model.Review
import com.yujin.record.domain.model.ReviewType
import kotlin.time.Clock
import kotlin.time.Instant

data class ReviewFormUiState(
    val overlay: Overlay = Overlay.None,

    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val type: ReviewType = ReviewType.MOVIE,
    val creatorName: String = "",
    val rating: Float = 3f,
    val images: List<ReviewImage> = emptyList(),
    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val selectedBookmarkId: Long = 0,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val posters: List<Video> = emptyList()
)

enum class Overlay {
    None,
    Progress,
    BottomSheet,
}

fun ReviewFormUiState.toReview() = Review(
    id = id,
    title = title.trim(),
    content = content.trim(),
    reviewType = type,
    creatorName = creatorName.trim(),
    rating = rating,
    images = images,
    tags = tags,
    bookmarkId = selectedBookmarkId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun Review.toUiState() = ReviewFormUiState(
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
    updatedAt = updatedAt,
)
