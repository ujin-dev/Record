package com.yujin.record.feature.review.edit

sealed interface ReviewEditUiEvent {
    data class NavigateToDetail(val id: Long) : ReviewEditUiEvent
    data class ShowToast(val message: String) : ReviewEditUiEvent
}