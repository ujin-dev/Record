package com.yujin.record.presentation.reviewdetail

sealed interface ReviewDetailScreenState {
    data object Loading : ReviewDetailScreenState
    data object NotFound : ReviewDetailScreenState
    data class Success(val review: ReviewDetailUiState) : ReviewDetailScreenState
}