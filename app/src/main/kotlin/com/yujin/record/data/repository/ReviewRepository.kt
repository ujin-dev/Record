package com.yujin.record.data.repository

import com.yujin.record.database.model.ReviewWithImages
import com.yujin.record.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

interface ReviewRepository {
    suspend fun insertReview(review:Review):Result<Long>
    suspend fun updateReview(review:Review):Result<Unit>
    suspend fun getReviewById(id: Long): Result<Review>
    suspend fun deleteReview(reviewId: Long): Result<Unit>
    fun observeReviewsByDate(date: LocalDate): Flow<List<ReviewWithImages>>
    fun observeReviewsByMonth(month: YearMonth): Flow<List<ReviewWithImages>>
}