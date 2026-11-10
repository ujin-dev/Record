package com.yujin.record.presentation.reviewdetail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yujin.record.domain.usecase.DeleteReviewUseCase
import com.yujin.record.domain.usecase.GetReviewByIdUseCase
import com.yujin.record.domain.usecase.ReviewResult
import com.yujin.record.navigation.RecordRoute
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
    private val getReviewByIdUseCase: GetReviewByIdUseCase,
    private val deleteReviewUseCase: DeleteReviewUseCase,
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

    private fun loadReview() {
        _uiState.value = ReviewDetailScreenState.Loading
        
        viewModelScope.launch {
            when (val result = getReviewByIdUseCase(reviewId)) {
                is ReviewResult.Success -> {
                    _uiState.value = ReviewDetailScreenState.Success(result.data.toUiState())
                }

                ReviewResult.Error -> {
                    _uiState.value = ReviewDetailScreenState.NotFound
                }
            }
        }
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
        when (deleteReviewUseCase(reviewId)) {
            is ReviewResult.Success -> _uiEvent.emit(ReviewDetailUiEvent.NavigateToHome)
            ReviewResult.Error -> {
                // todo: show toast
            }
        }
    }
}
