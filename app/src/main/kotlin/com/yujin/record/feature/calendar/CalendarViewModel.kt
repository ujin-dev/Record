package com.yujin.record.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yujin.record.core.data.mapper.toReview
import com.yujin.record.core.data.repository.ReviewRepository
import com.yujin.record.core.model.Review
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
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
import kotlin.math.ceil
import kotlin.time.Clock

class CalendarViewModel(
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val selectedDate = MutableStateFlow(
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    )

    private val selectedMonth = MutableStateFlow(selectedDate.value.toYearMonth())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val monthlyReviews =
        selectedMonth
            .flatMapLatest { month ->
                reviewRepository.observeReviewsByMonth(month)
            }.map { list ->
                list.map { it.toReview() }
            }

    private val calendarDays =
        combine(
            selectedMonth,
            monthlyReviews,
        ) { month, reviews ->
            buildCalendar(month, reviews)
        }

    val uiState: StateFlow<CalendarUiState> =
        combine(
            selectedMonth,
            selectedDate,
            monthlyReviews,
            calendarDays,
        ) { month, date, reviews, calendarDays ->
            CalendarUiState(
                month = month,
                selectedDate = date,
                calendarDays = calendarDays,
                reviews = reviews,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CalendarUiState(
                month = selectedMonth.value,
                selectedDate = selectedDate.value,
            )
        )

    private val _uiEvent = MutableSharedFlow<CalendarUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    companion object {
        fun factory(reviewRepository: ReviewRepository): ViewModelProvider.Factory =
            viewModelFactory {
                initializer { CalendarViewModel(reviewRepository) }
            }
    }

    fun onAction(action: CalendarUiAction) {
        when (action) {
            is CalendarUiAction.PreviousMonthClicked -> onPreviousMonthClick()
            is CalendarUiAction.NextMonthClicked -> onNextMonthClick()
            is CalendarUiAction.DateClicked -> onDateClick(action.date)
            is CalendarUiAction.CreateReviewClicked -> onCreateReviewClick()
            is CalendarUiAction.ReviewClicked -> onReviewClick(action.reviewId)
        }
    }

    fun onPreviousMonthClick() {
        selectedMonth.update { it.minusMonth() }
    }

    fun onNextMonthClick() {
        selectedMonth.update { it.plusMonth() }
    }

    fun onDateClick(date: LocalDate) {
        // TODO
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

    fun buildCalendar(
        month: YearMonth,
        reviews: List<Review>,
    ): List<CalendarCellUiState> {
        val firstDay = month.firstDay()
        val startOffset = firstDay.dayOfWeek.toJavaDayOfWeek().value % 7
        val daysInMonth = month.numberOfDays
        val weekCount = ceil((startOffset + daysInMonth) / 7f).toInt()
        val totalCellCount = weekCount * 7
        val startDate = firstDay - DatePeriod(days = startOffset)
        val reviewsByDate = reviews.groupBy {
            it.createdAt.toLocalDateTime(TimeZone.currentSystemDefault()).date
        }

        return (0 until totalCellCount).map { index ->
            val date = startDate + DatePeriod(days = index)

            if (date.toYearMonth() == month) {
                CalendarCellUiState.Active(
                    date = date,
                    reviewCount = reviewsByDate[date]?.size ?: 0,
                    thumbnailUrl = null, // TODO
                )
            } else {
                CalendarCellUiState.Empty
            }
        }
    }
}