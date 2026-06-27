package com.yujin.record.feature.calendar

import com.yujin.record.core.model.Review
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

data class CalendarUiState(
    val month: YearMonth,
    val selectedDate: LocalDate,
    val calendarDays: List<CalendarDayUiState> = emptyList(),
    val reviews: List<Review> = emptyList()
)

data class CalendarDayUiState(
    val date: LocalDate,
    val isSelected: Boolean = false,
    val reviewCount: Int,
    val thumbnailUrl: String?
)