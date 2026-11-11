package com.yujin.record.domain.usecase

import com.yujin.record.domain.model.Review
import com.yujin.record.domain.repository.ReviewRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SaveReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(review: Review): ReviewResult<Long> = try {
        val reviewId = if (review.id == 0L) {
            reviewRepository.insertReview(review)
        } else {
            reviewRepository.updateReview(review)
            review.id
        }
        ReviewResult.Success(reviewId)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ReviewResult.Error
    }
}
