package com.yujin.record.feature.review.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yujin.record.core.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ReviewDetailViewModel(
    private val reviewId: Long,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val _uiState =
        MutableStateFlow<ReviewDetailScreenState>(ReviewDetailScreenState.Loading)
    val uiState: StateFlow<ReviewDetailScreenState> = _uiState

    companion object {
        // todo : Integrate Hilt
        fun Factory(
            reviewId: Long,
            reviewRepository: ReviewRepository
        ): ViewModelProvider.Factory =
            viewModelFactory {
                initializer {
                    ReviewDetailViewModel(
                        reviewId = reviewId,
                        reviewRepository = reviewRepository
                    )
                }
            }
    }

    init {
        loadReview()
    }

    private fun loadReview() {
        viewModelScope.launch {
            _uiState.value = ReviewDetailScreenState.Loading

            val result = reviewRepository.getReviewById(reviewId)
            _uiState.value = if (result != null) {
                ReviewDetailScreenState.Success(result.toUiState())
            } else {
                ReviewDetailScreenState.NotFound
            }
        }
    }
}