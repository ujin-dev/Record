package com.yujin.record.core.data.repository

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.room.withTransaction
import com.yujin.record.core.data.ImageStorage
import com.yujin.record.core.data.mapper.toEntity
import com.yujin.record.core.data.mapper.toReview
import com.yujin.record.core.database.AppDatabase
import com.yujin.record.core.database.dao.ImageDao
import com.yujin.record.core.database.dao.ReviewDao
import com.yujin.record.core.database.model.ReviewWithImages
import com.yujin.record.core.model.Review
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.atStartOfDayIn
import kotlinx.datetime.plus
import kotlinx.datetime.toKotlinTimeZone
import java.time.ZoneId
import javax.inject.Inject
import kotlin.time.Clock

class ReviewRepositoryImpl @Inject constructor(
    private val database: AppDatabase,
    private val reviewDao: ReviewDao,
    private val imageDao: ImageDao,
    private val imageStorage: ImageStorage,
) : ReviewRepository {

    override suspend fun upsertReview(review: Review): Result<Long> =
        runCatching {
            val backupUris = mutableListOf<Uri>()
            val isCreate = review.id == 0L

            try {
                database.withTransaction {
                    val now = Clock.System.now()

                    val reviewId = reviewDao.upsert(
                        review.toEntity(
                            createdAt = if (isCreate) now else review.createdAt,
                            updatedAt = now
                        )
                    ).takeIf { it != -1L } ?: review.id

                    val oldImages =
                        if (isCreate) listOf()
                        else imageDao.getByReviewId(review.id)

                    val deleteUris =
                        if (oldImages.isNotEmpty()) {
                            oldImages.map { it.uri.toUri() }.toSet() -
                                    review.images.map { it.uri }.toSet()
                        } else {
                            emptySet()
                        }

                    val imageEntities = review.images.mapIndexed { index, item ->
                        val backupUri =
                            if (imageStorage.isBackupImage(item.uri)) {
                                item.uri
                            } else {
                                imageStorage.backupImage(item.uri).also(backupUris::add)
                            }

                        item.copy(uri = backupUri).toEntity(reviewId, index + 1)
                    }

                    if (oldImages.isNotEmpty()) {
                        imageDao.deleteAll(oldImages)
                    }
                    imageDao.insertAll(imageEntities)

                    if (deleteUris.isNotEmpty()) {
                        imageStorage.deleteImages(deleteUris.toList())
                    }

                    reviewId
                }
            } catch (e: Exception) {
                runCatching {
                    if (backupUris.isNotEmpty()) {
                        imageStorage.deleteImages(backupUris)
                    }
                }.onFailure {
                    Log.e(
                        "ReviewRepositoryImpl",
                        "Failed to delete images - $backupUris"
                    )
                }

                throw e
            }
        }

    override suspend fun getReviewById(id: Long): Result<Review> = runCatching {
        reviewDao.getById(id)?.toReview() ?: throw NoSuchElementException()
    }

    override suspend fun deleteReview(reviewId: Long): Result<Unit> = runCatching {
        database.withTransaction {
            val images = imageDao.getByReviewId(reviewId)
            if (images.isNotEmpty()) {
                imageStorage.deleteImages(images.map { it.uri.toUri() })
            }
            reviewDao.deleteById(reviewId)
        }
    }

    override fun observeReviewsByDate(date: LocalDate): Flow<List<ReviewWithImages>> {
        val zoneId = ZoneId.systemDefault()
        val start = date.atStartOfDayIn(zoneId.toKotlinTimeZone())
        val end = date.plus(period = DatePeriod(days = 1)).atStartOfDayIn(zoneId.toKotlinTimeZone())
        return reviewDao.observeByDateRange(start, end)
    }

    override fun observeReviewsByMonth(month: YearMonth): Flow<List<ReviewWithImages>> {
        val zoneId = ZoneId.systemDefault()
        val start = month.firstDay.atStartOfDayIn(zoneId.toKotlinTimeZone())
        val end = month.lastDay.plus(period = DatePeriod(days = 1))
            .atStartOfDayIn(zoneId.toKotlinTimeZone())
        return reviewDao.observeByDateRange(start, end)
    }
}