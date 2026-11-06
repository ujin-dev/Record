package com.yujin.record.reviewform

sealed interface ReviewFormUiEvent {
    object NavigateToBack : ReviewFormUiEvent
    data class NavigateToDetail(val id: Long) : ReviewFormUiEvent
}