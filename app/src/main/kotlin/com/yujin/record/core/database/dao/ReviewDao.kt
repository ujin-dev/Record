package com.yujin.record.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import androidx.room.Upsert
import com.yujin.record.core.database.model.ReviewEntity
import com.yujin.record.core.database.model.ReviewWithImages
import kotlinx.coroutines.flow.Flow
import kotlin.time.Instant

@Dao
interface ReviewDao {
    @Insert
    suspend fun insert(review: ReviewEntity): Long

    @Update
    suspend fun update(review: ReviewEntity): Unit

    @Query("DELETE FROM reviews WHERE id = :reviewId")
    suspend fun deleteById(reviewId: Long): Int

    @Transaction
    @Query("SELECT * FROM reviews")
    suspend fun getAll(): List<ReviewWithImages>

    @Transaction
    @Query("SELECT * FROM reviews WHERE id = :reviewId")
    suspend fun getById(reviewId: Long): ReviewWithImages?

    @Transaction
    @Query("SELECT * FROM reviews WHERE created_at BETWEEN :start AND :end")
    fun observeByDateRange(start: Instant, end: Instant): Flow<List<ReviewWithImages>>
}