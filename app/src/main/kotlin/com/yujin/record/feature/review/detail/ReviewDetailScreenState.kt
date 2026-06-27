package com.yujin.record.feature.review.detail

sealed interface ReviewDetailScreenState {
    data object Loading : ReviewDetailScreenState
    data object NotFound : ReviewDetailScreenState
    data class Success(val review: ReviewDetailUiState) : ReviewDetailScreenState
}