package com.yujin.record.domain.usecase

import com.yujin.record.domain.model.Review
import com.yujin.record.domain.repository.ReviewRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class UpdateReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(review: Review): ReviewResult<Long> = try {
        reviewRepository.updateReview(review)
        ReviewResult.Success(review.id)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ReviewResult.Error
    }
}
