package com.yujin.record.database.model

import androidx.room.Embedded
import androidx.room.Relation

data class ReviewWithImages(
    @Embedded
    val review: ReviewEntity,

    @Relation(
        parentColumn = "id",
        entityColumn = "review_id"
    )
    val images: List<ImageEntity>
)