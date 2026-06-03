package com.yujin.record.core.data.mapper

import com.yujin.record.core.database.model.ReviewEntity
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
