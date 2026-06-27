package com.yujin.record.feature.calendar

sealed interface CalendarUiEvent {
    data object NavigateToCreate : CalendarUiEvent
    data class NavigateToDetail(val reviewId: Long) : CalendarUiEvent
}