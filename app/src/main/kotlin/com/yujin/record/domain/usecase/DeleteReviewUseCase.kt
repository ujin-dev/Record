package com.yujin.record.domain.usecase

import com.yujin.record.domain.repository.ReviewRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class DeleteReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(reviewId: Long): ReviewResult<Unit> = try {
        reviewRepository.deleteReview(reviewId)
        ReviewResult.Success(Unit)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ReviewResult.Error
    }
}
