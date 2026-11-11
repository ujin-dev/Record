package com.yujin.record.data.database.mapper

import androidx.core.net.toUri
import com.yujin.record.data.database.model.ImageEntity
import com.yujin.record.domain.model.ReviewImage

fun ReviewImage.toEntity(reviewId: Long, order: Int) = ImageEntity(
    id = id,
    reviewId = reviewId,
    uri = uri.toString(),
    displayOrder = order,
)

fun ImageEntity.toImageItem() = ReviewImage(
    id = id,
    uri = uri.toUri(),
)