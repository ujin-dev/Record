package com.yujin.record.presentation.reviewdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yujin.record.navigation.RecordRoute
import com.yujin.record.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
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

    private val _uiEvent = MutableSharedFlow<ReviewDetailUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        loadReview()
    }

    private fun loadReview() = viewModelScope.launch {
        _uiState.value = ReviewDetailScreenState.Loading

        val result = reviewRepository.getReviewById(reviewId)
        _uiState.value = result.fold(
            onSuccess = { review -> ReviewDetailScreenState.Success(review.toUiState()) },
            onFailure = { ReviewDetailScreenState.NotFound }
        )
    }

    fun onAction(action: ReviewDetailUiAction) {
        when (action) {
            ReviewDetailUiAction.BackClicked -> backStack()
            ReviewDetailUiAction.EditClicked -> editReview()
            ReviewDetailUiAction.DeleteClicked -> deleteReview()
        }
    }

    private fun backStack() = viewModelScope.launch {
        _uiEvent.emit(ReviewDetailUiEvent.NavigateToBack)
    }

    private fun editReview() = viewModelScope.launch {
        _uiEvent.emit(ReviewDetailUiEvent.NavigateToForm(reviewId))
    }

    private fun deleteReview() = viewModelScope.launch {
        reviewRepository.deleteReview(reviewId)
            .onSuccess {
                _uiEvent.emit(ReviewDetailUiEvent.NavigateToHome)
            }.onFailure {
                // todo: show toast
            }
    }
}