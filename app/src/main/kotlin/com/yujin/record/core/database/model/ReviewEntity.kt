package com.yujin.record.core.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.yujin.record.core.model.ReviewType
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
@Entity(tableName = "reviews")
data class ReviewEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,

    val title: String,
    @ColumnInfo(name = "creator_name") val creatorName: String = "",
    @ColumnInfo(name = "short_review") val shortReview: String = "",
    val content: String = "",
    @ColumnInfo(name = "type") val reviewType: ReviewType,
    val tags: List<String> = emptyList(),
    val rating: Float,
    @ColumnInfo(name = "bookmark_id") val bookmarkId: Long?,

    @ColumnInfo(name = "created_at") val createdAt: Instant,
    @ColumnInfo(name = "updated_at") val updatedAt: Instant,
)