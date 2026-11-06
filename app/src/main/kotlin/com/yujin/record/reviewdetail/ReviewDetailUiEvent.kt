package com.yujin.record.reviewdetail

sealed interface ReviewDetailUiEvent {
    object NavigateToBack : ReviewDetailUiEvent
    object NavigateToHome : ReviewDetailUiEvent
    data class NavigateToForm(val id: Long) : ReviewDetailUiEvent
}