package com.yujin.record.core.data.mapper

import androidx.core.net.toUri
import com.yujin.record.core.database.model.ImageEntity
import com.yujin.record.core.model.ImageItem

fun ImageItem.toEntity(reviewId: Long, order: Int) = ImageEntity(
    id = id,
    reviewId = reviewId,
    uri = uri.toString(),
    displayOrder = order,
)

fun ImageEntity.toImageItem() = ImageItem(
    id = id,
    uri = uri.toUri(),
)