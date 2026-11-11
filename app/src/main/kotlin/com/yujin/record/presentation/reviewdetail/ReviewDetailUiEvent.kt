package com.yujin.record.presentation.reviewdetail

sealed interface ReviewDetailUiEvent {
    object NavigateToBack : ReviewDetailUiEvent
    object NavigateToHome : ReviewDetailUiEvent
    data class NavigateToForm(val id: Long) : ReviewDetailUiEvent
    data class ShowSnackbar(val messageRes: Int) : ReviewDetailUiEvent
}
