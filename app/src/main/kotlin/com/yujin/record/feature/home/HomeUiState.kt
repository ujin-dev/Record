package com.yujin.record.feature.home

import com.yujin.record.model.Review
import java.time.LocalDate
import java.time.YearMonth

data class HomeUiState(
    val month: YearMonth = YearMonth.now(),
    val selectedDate: LocalDate = LocalDate.now(),
    val calendarDays: List<CalendarDayUiState> = emptyList(),
    val selectedDateReviews: List<Review> = emptyList()
)

data class CalendarDayUiState(
    val date: LocalDate,
    val isSelected: Boolean = false,
    val reviewCount: Int,
    val thumbnailUrl: String?
)