package com.yujin.record.reviewdetail

sealed interface ReviewDetailScreenState {
    data object Loading : ReviewDetailScreenState
    data object NotFound : ReviewDetailScreenState
    data class Success(val review: ReviewDetailUiState) : ReviewDetailScreenState
}