package com.yujin.record.core.data.repository

import androidx.room.Transaction
import com.yujin.record.core.data.mapper.toEntity
import com.yujin.record.core.data.mapper.toReview
import com.yujin.record.core.database.dao.ImageDao
import com.yujin.record.core.database.dao.ReviewDao
import com.yujin.record.core.database.model.ReviewWithImages
import com.yujin.record.core.model.Review
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toKotlinTimeZone
import java.time.ZoneId
import kotlin.time.Clock

class ReviewRepositoryImpl(
    private val reviewDao: ReviewDao,
    private val imageDao: ImageDao,
) : ReviewRepository {

    @Transaction
    override suspend fun createReview(review: Review): Result<Long> =
        runCatching {
            val now = Clock.System.now()
            val reviewId = reviewDao.insert(review.toEntity(createdAt = now, updatedAt = now))

            imageDao.insertAll(
                review.images.mapIndexed { index, item ->
                    item.toEntity(reviewId, index + 1)
                }
            )

            reviewId
        }

    override suspend fun updateReview(review: Review): Result<Int> =
        runCatching {
            val now = Clock.System.now()
            val updatedCount =
                reviewDao.update(review.toEntity(createdAt = review.createdAt, updatedAt = now))

            imageDao.updateAll(
                review.images.mapIndexed { index, item ->
                    item.toEntity(reviewId = review.id, order = index + 1)
                }
            )

            updatedCount
        }

    override suspend fun getReviewById(id: Long): Review? {
        return reviewDao.getById(id)?.toReview()
    }

    override suspend fun getReviewsByDate(date: LocalDate): List<ReviewWithImages> {
        val zoneId = ZoneId.systemDefault()
        val start = date.atStartOfDayIn(zoneId.toKotlinTimeZone())
        val end = date.plus(period = DatePeriod(days = 1)).atStartOfDayIn(zoneId.toKotlinTimeZone())
        return reviewDao.getByDateRange(start, end)
    }

    override suspend fun getReviewsByMonth(month: YearMonth): List<ReviewWithImages> {
        val zoneId = ZoneId.systemDefault()
        val start = month.firstDay.atStartOfDayIn(zoneId.toKotlinTimeZone())
        val end = month.lastDay.plus(period = DatePeriod(days = 1))
            .atStartOfDayIn(zoneId.toKotlinTimeZone())
        return reviewDao.getByDateRange(start, end)
    }
}