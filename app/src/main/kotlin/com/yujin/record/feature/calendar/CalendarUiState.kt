package com.yujin.record.feature.calendar

import com.yujin.record.core.model.Review
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

data class CalendarUiState(
    val calendarState : CalendarState,
    val reviews: List<Review> = emptyList(),
)

data class CalendarState(
    val displayedYearMonth: YearMonth,
    val selectedDate: LocalDate,
    val calendarDays: List<CalendarCellUiState> = emptyList(),
)

sealed interface CalendarCellUiState {
    data class Active(
        val date: LocalDate,
        val reviewCount: Int,
        val thumbnailUrl: String?,
    ) : CalendarCellUiState

    data object Empty : CalendarCellUiState
}