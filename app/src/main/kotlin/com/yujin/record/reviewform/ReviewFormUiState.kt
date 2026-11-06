package com.yujin.record.reviewform

import com.yujin.record.model.ImageItem
import com.yujin.record.model.Review
import com.yujin.record.model.ReviewType
import kotlin.time.Clock
import kotlin.time.Instant

enum class Overlay {
    None,
    Progress,
    BottomSheet,
}

data class ReviewFormUiState(
    val overlay: Overlay = Overlay.None,

    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val type: ReviewType = ReviewType.MOVIE,
    val creatorName: String = "",
    val rating: Float = 3f,
    val images: List<ImageItem> = emptyList(),
    val tags: List<String> = emptyList(),
    val tagInput: String = "",
    val selectedBookmarkId: Long = 0,
    val createdAt: Instant = Clock.System.now(),
    val updatedAt: Instant = Clock.System.now(),
    val posters: List<MediaItem> = emptyList()
)

data class MediaItem(
    val id: Int,
    val type: ReviewType,
    val title: String,
    val posterPath: String? = ""
)

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