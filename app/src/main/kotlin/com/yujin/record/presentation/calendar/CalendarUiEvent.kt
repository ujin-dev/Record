package com.yujin.record.presentation.calendar

sealed interface CalendarUiEvent {
    data class NavigateToCreate(val createdDate: String) : CalendarUiEvent
    data class NavigateToDetail(val reviewId: Long) : CalendarUiEvent
}