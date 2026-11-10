package com.yujin.record.domain.usecase

import com.yujin.record.domain.model.Review
import com.yujin.record.domain.repository.ReviewRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class InsertReviewUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(review: Review): ReviewResult<Long> = try {
        ReviewResult.Success(reviewRepository.insertReview(review))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ReviewResult.Error
    }
}
