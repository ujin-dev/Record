package com.yujin.record.feature.home

import androidx.lifecycle.ViewModel
import com.yujin.record.model.Review
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate
import java.time.YearMonth
import kotlin.math.ceil

class HomeViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadMonth(YearMonth.now())
    }

    fun onPreviousMonthClick() {
        loadMonth(_uiState.value.month.minusMonths(1))
    }

    fun onNextMonthClick() {
        loadMonth(_uiState.value.month.plusMonths(1))
    }

    fun onDateClick(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                selectedDate = date,
                selectedDateReviews = loadReview(date)
            )
        }
    }

    fun onCreateReviewClick() {
        // TODO
    }

    fun loadMonth(month: YearMonth) {
        _uiState.update { state ->
            state.copy(
                month = month,
                calendarDays = buildCalendar(month, state.selectedDate),
                selectedDateReviews = loadReview(state.selectedDate)
            )
        }
    }

    fun buildCalendar(month: YearMonth, selectedDate: LocalDate): List<CalendarDayUiState> {
        val firstDay = month.atDay(1)
        val startOffset = firstDay.dayOfWeek.value % 7
        val daysInMonth = month.lengthOfMonth()
        val weekCount = ceil((startOffset + daysInMonth) / 7f).toInt()
        val totalCellCount = weekCount * 7
        val startDate = firstDay.minusDays(startOffset.toLong())

        return (0 until totalCellCount).map { index ->
            val date = startDate.plusDays(index.toLong())
            CalendarDayUiState(
                date = date,
                isSelected = date == selectedDate,
                reviewCount = 0, // TODO
                thumbnailUrl = null // TODO
            )
        }
    }

    fun loadReview(date: LocalDate): List<Review> {
        // TODO
        return emptyList()
    }
}