package com.yujin.record.feature.review.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yujin.record.app.navigation.RecordRoute
import com.yujin.record.core.data.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val reviewId: Long = savedStateHandle.toRoute<RecordRoute.ReviewDetail>().reviewId

    private val _uiState =
        MutableStateFlow<ReviewDetailScreenState>(ReviewDetailScreenState.Loading)
    val uiState: StateFlow<ReviewDetailScreenState> = _uiState

    init {
        loadReview()
    }

    private fun loadReview() = viewModelScope.launch {
        _uiState.value = ReviewDetailScreenState.Loading

        val result = reviewRepository.getReviewById(reviewId)
        _uiState.value = if (result != null) {
            ReviewDetailScreenState.Success(result.toUiState())
        } else {
            ReviewDetailScreenState.NotFound
        }
    }
}