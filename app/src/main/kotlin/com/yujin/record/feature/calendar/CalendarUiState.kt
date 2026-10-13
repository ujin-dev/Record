package com.yujin.record.feature.calendar

import android.net.Uri
import com.yujin.record.core.model.Review
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth

data class CalendarUiState(
    val calendarState: CalendarContentUiState,
    val monthlyReviews: List<Review> = emptyList(),
)

data class CalendarContentUiState(
    val displayedYearMonth: YearMonth,
    val cellStatesByDate: Map<LocalDate, CalendarCellUiState> = emptyMap()
)

data class CalendarCellUiState(
    val date: LocalDate,
    val reviewCount: Int = 0,
    val thumbnailUrl: Uri? = null,
)