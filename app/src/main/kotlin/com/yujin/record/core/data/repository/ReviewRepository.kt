package com.yujin.record.core.data.repository

import com.yujin.record.core.model.Review
import kotlinx.coroutines.flow.Flow

interface ReviewRepository {
    suspend fun createReview(review: Review): Result<Long>
    suspend fun updateReview(review: Review): Result<Int>
    fun getReviewById(id: Long): Flow<Review?>
}