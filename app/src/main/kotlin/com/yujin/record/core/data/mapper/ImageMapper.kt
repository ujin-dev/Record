package com.yujin.record.core.data.mapper

import com.yujin.record.core.database.model.ImageEntity
import com.yujin.record.core.model.ImageItem

fun ImageItem.toEntity(reviewId: Long, order: Int) = ImageEntity(
    id = id,
    reviewId = reviewId,
    uri = uri.toString(),
    displayOrder = order,
)