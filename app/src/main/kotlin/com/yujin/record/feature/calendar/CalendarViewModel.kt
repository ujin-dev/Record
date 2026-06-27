package com.yujin.record.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yujin.record.core.data.mapper.toReview
import com.yujin.record.core.data.repository.ReviewRepository
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.minusMonth
import kotlinx.datetime.plus
import kotlinx.datetime.plusMonth
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toLocalDateTime
import kotlinx.datetime.yearMonth
import kotlin.math.ceil
import kotlin.time.Clock

class CalendarViewModel(
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val today = Clock.System.now()
        .toLocalDateTime(TimeZone.currentSystemDefault())
        .date

    private val _uiState = MutableStateFlow(
        CalendarUiState(
            month = today.yearMonth,
            selectedDate = today,
        )
    )
    val uiState: StateFlow<CalendarUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CalendarUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    companion object {
        fun factory(reviewRepository: ReviewRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { CalendarViewModel(reviewRepository) }
            }
    }

    init {
        loadMonth(month = _uiState.value.month)
        loadReview(month = _uiState.value.month)
    }

    fun onAction(action: CalendarUiAction) {
        when (action) {
            is CalendarUiAction.DateClicked -> onDateClick(action.date)
            is CalendarUiAction.MonthChanged -> loadMonth(action.month)
            is CalendarUiAction.PreviousMonthClicked -> onPreviousMonthClick()
            is CalendarUiAction.NextMonthClicked -> onNextMonthClick()
            is CalendarUiAction.CreateReviewClicked -> onCreateReviewClick()
            is CalendarUiAction.ReviewClicked -> onReviewClick(action.reviewId)
        }
    }

    fun onPreviousMonthClick() {
        loadMonth(_uiState.value.month.minusMonth())
    }

    fun onNextMonthClick() {
        loadMonth(_uiState.value.month.plusMonth())
    }

    fun onDateClick(date: LocalDate) {
        _uiState.update { state ->
            state.copy(
                selectedDate = date,
            )
        }
    }

    fun onCreateReviewClick() {
        viewModelScope.launch {
            _uiEvent.emit(CalendarUiEvent.NavigateToCreate)
        }
    }

    fun onReviewClick(reviewId: Long) {
        viewModelScope.launch {
            _uiEvent.emit(CalendarUiEvent.NavigateToDetail(reviewId))
        }
    }

    fun loadMonth(month: YearMonth) {
        _uiState.update { state ->
            state.copy(
                month = month,
                calendarDays = buildCalendar(month, state.selectedDate),
            )
        }
    }

    fun buildCalendar(month: YearMonth, selectedDate: LocalDate): List<CalendarDayUiState> {
        val firstDay = month.firstDay()
        val startOffset = firstDay.dayOfWeek.toJavaDayOfWeek().value % 7
        val daysInMonth = month.numberOfDays
        val weekCount = ceil((startOffset + daysInMonth) / 7f).toInt()
        val totalCellCount = weekCount * 7
        val startDate = firstDay - DatePeriod(days = startOffset)

        return (0 until totalCellCount).map { index ->
            val date = startDate + DatePeriod(days = index)
            CalendarDayUiState(
                date = date,
                isSelected = date == selectedDate,
                reviewCount = 0, // TODO
                thumbnailUrl = null // TODO
            )
        }
    }

    fun loadReview(month: YearMonth) {
        viewModelScope.launch {
            val reviews = reviewRepository.getReviewsByMonth(month).map { it.toReview() }
            _uiState.update {
                it.copy(reviews = reviews)
            }
        }
    }
}