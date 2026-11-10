package com.yujin.record.data.database.util

import androidx.room.TypeConverter
import com.yujin.record.domain.model.ReviewType
import kotlinx.serialization.json.Json
import kotlin.time.ExperimentalTime
import kotlin.time.Instant

@OptIn(ExperimentalTime::class)
class Converters {
    @TypeConverter
    fun fromReviewType(reviewType: ReviewType): String = reviewType.name

    @TypeConverter
    fun toReviewType(value: String): ReviewType = ReviewType.valueOf(value)

    @TypeConverter
    fun fromStringList(list: List<String>): String = Json.encodeToString(list)

    @TypeConverter
    fun toStringList(value: String): List<String> = Json.Default.decodeFromString(value)

    @TypeConverter
    fun fromInstant(instant: Instant): Long = instant.toEpochMilliseconds()

    @TypeConverter
    fun toInstant(millis: Long): Instant = Instant.fromEpochMilliseconds(millis)
}