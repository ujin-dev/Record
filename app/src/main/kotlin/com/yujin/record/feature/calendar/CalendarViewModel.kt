package com.yujin.record.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.yujin.record.core.data.mapper.toReview
import com.yujin.record.core.data.repository.ReviewRepository
import com.yujin.record.core.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
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
import kotlinx.coroutines.launch
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlinx.datetime.toJavaDayOfWeek
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.math.ceil
import kotlin.time.Clock

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val selectedDate = MutableStateFlow(
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    )

    private val currentMonth = MutableStateFlow(selectedDate.value.toYearMonth())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val reviewsForMonth =
        currentMonth
            .flatMapLatest { month ->
                reviewRepository.observeReviewsByMonth(month)
            }.map { list ->
                list.map { it.toReview() }
            }

    val uiState: StateFlow<CalendarUiState> =
        combine(
            currentMonth,
            selectedDate,
            reviewsForMonth,
        ) { month, date, reviews ->
            CalendarUiState(
                calendarState = CalendarState(
                    displayedYearMonth = month,
                    selectedDate = date,
                    calendarDays = buildCalendar(month, reviews),
                ),
                reviews = reviews,
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CalendarUiState(
                calendarState = CalendarState(
                    displayedYearMonth = currentMonth.value,
                    selectedDate = selectedDate.value,
                    calendarDays = buildCalendar(currentMonth.value, emptyList()),
                ),
                reviews = emptyList()
            )
        )

    private val _uiEvent = MutableSharedFlow<CalendarUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    fun onAction(action: CalendarUiAction) {
        when (action) {
            is CalendarUiAction.MonthChanged -> onMonthChange(action.month)
            is CalendarUiAction.DateClicked -> onDateClick(action.date)
            is CalendarUiAction.CreateReviewClicked -> onCreateReviewClick()
            is CalendarUiAction.ReviewClicked -> onReviewClick(action.reviewId)
        }
    }

    fun onMonthChange(month: YearMonth) {
        currentMonth.value = month
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