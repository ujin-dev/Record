package com.yujin.record.core.data.mapper

import com.yujin.record.core.database.model.ReviewEntity
import com.yujin.record.core.database.model.ReviewWithImages
import com.yujin.record.core.model.Review
import kotlin.time.Instant

fun Review.toEntity(createdAt: Instant, updatedAt: Instant) = ReviewEntity(
    id = id,
    title = title,
    creatorName = creatorName,
    shortReview = shortReview,
    content = content,
    reviewType = reviewType,
    tags = tags,
    rating = rating,
    bookmarkId = bookmarkId,
    createdAt = createdAt,
    updatedAt = updatedAt,
)

fun ReviewWithImages.toReview() = Review(
    id = review.id,
    title = review.title,
    creatorName = review.creatorName,
    shortReview = review.shortReview,
    content = review.content,
    reviewType = review.reviewType,
    rating = review.rating,
    images = emptyList(), // todo
    tags = review.tags,
    bookmarkId = review.bookmarkId ?: 0,
    createdAt = review.createdAt,
    updatedAt = review.updatedAt,
)