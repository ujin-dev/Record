package com.yujin.record.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.yujin.record.database.model.ImageEntity

@Dao
interface ImageDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(images: List<ImageEntity>): List<Long>

    @Update
    suspend fun updateAll(images: List<ImageEntity>): Int

    @Delete
    suspend fun deleteAll(images: List<ImageEntity>): Int

    @Query(
        value = """
            SELECT * FROM images 
            WHERE review_id = :reviewId 
            ORDER BY display_order ASC
        """,
    )
    suspend fun getByReviewId(reviewId: Long): List<ImageEntity>
}