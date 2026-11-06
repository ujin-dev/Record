package com.yujin.record.data.mapper

import com.yujin.record.database.model.ReviewEntity
import com.yujin.record.database.model.ReviewWithImages
import com.yujin.record.model.Review

fun Review.toEntity() =
    ReviewEntity(
        id = id,
        title = title,
        creatorName = creatorName,
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
    content = review.content,
    reviewType = review.reviewType,
    rating = review.rating,
    images = images.map { it.toImageItem() },
    tags = review.tags,
    bookmarkId = review.bookmarkId ?: 0,
    createdAt = review.createdAt,
    updatedAt = review.updatedAt,
)