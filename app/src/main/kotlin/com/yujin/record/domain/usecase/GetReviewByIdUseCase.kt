package com.yujin.record.domain.usecase

import com.yujin.record.domain.model.Review
import com.yujin.record.domain.repository.ReviewRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GetReviewByIdUseCase @Inject constructor(
    private val reviewRepository: ReviewRepository,
) {
    suspend operator fun invoke(id: Long): ReviewResult<Review> = try {
        ReviewResult.Success(reviewRepository.getReviewById(id))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        ReviewResult.Error
    }
}
