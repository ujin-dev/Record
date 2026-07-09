package com.yujin.record.feature.review.form

sealed interface ReviewFormUiEvent {
    object NavigateToBack : ReviewFormUiEvent
    data class NavigateToDetail(val id: Long) : ReviewFormUiEvent
    data class ShowToast(val message: String) : ReviewFormUiEvent
}