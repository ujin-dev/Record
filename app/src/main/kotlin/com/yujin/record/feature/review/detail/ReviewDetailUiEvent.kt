package com.yujin.record.feature.review.detail

sealed interface ReviewDetailUiEvent {
    object NavigateToBack : ReviewDetailUiEvent
    data class NavigateToForm(val id: Long) : ReviewDetailUiEvent
}