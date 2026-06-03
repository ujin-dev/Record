package com.yujin.record.core.data.repository

import androidx.room.Transaction
import com.yujin.record.core.data.mapper.toEntity
import com.yujin.record.core.database.dao.ImageDao
import com.yujin.record.core.database.dao.ReviewDao
import com.yujin.record.core.model.Review
import kotlinx.coroutines.flow.Flow
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

    override fun getReviewById(id: Long): Flow<Review?> {
        TODO("Not yet implemented")
    }
}