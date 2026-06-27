package com.yujin.record.core.data.repository

import com.yujin.record.core.database.model.ReviewWithImages
import com.yujin.record.core.model.Review
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

interface ReviewRepository {
    suspend fun createReview(review: Review): Result<Long>
    suspend fun updateReview(review: Review): Result<Int>
    suspend fun getReviewById(id: Long): Review?
    suspend fun getReviewsByDate(date: LocalDate): List<ReviewWithImages>
    suspend fun getReviewsByMonth(month: YearMonth): List<ReviewWithImages>
}