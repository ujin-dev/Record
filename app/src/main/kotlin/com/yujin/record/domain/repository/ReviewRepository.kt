package com.yujin.record.domain.repository

import com.yujin.record.domain.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

interface ReviewRepository {
    suspend fun insertReview(review:Review):Result<Long>
    suspend fun updateReview(review:Review):Result<Unit>
    suspend fun getReviewById(id: Long): Result<Review>
    suspend fun deleteReview(reviewId: Long): Result<Unit>
    fun observeReviewsByDate(date: LocalDate): Flow<List<Review>>
    fun observeReviewsByMonth(month: YearMonth): Flow<List<Review>>
}