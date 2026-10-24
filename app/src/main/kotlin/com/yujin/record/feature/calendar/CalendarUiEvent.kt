package com.yujin.record.feature.calendar

sealed interface CalendarUiEvent {
    data class NavigateToCreate(val createdDate: String) : CalendarUiEvent
    data class NavigateToDetail(val reviewId: Long) : CalendarUiEvent
}