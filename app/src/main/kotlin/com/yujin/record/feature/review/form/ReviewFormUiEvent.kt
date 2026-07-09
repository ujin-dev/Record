package com.yujin.record.feature.review.form

sealed interface ReviewFormUiEvent {
    data class NavigateToDetail(val id: Long) : ReviewFormUiEvent
    data class ShowToast(val message: String) : ReviewFormUiEvent
}