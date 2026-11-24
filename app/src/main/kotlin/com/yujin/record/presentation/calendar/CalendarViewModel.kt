package com.yujin.record.presentation.calendar

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yujin.record.domain.model.Review
import com.yujin.record.domain.repository.ReviewRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.todayIn
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private var today: LocalDate = Clock.System.todayIn(TimeZone.currentSystemDefault())

    private val currentMonth = MutableStateFlow(today.toYearMonth())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val monthlyReviews = currentMonth
        .flatMapLatest { month -> reviewRepository.observeReviewsByMonth(month) }

    private val _uiState = MutableStateFlow(
        CalendarUiState(
            selectedDate = today,
            calendarState = CalendarContentUiState(
                displayedYearMonth = today.toYearMonth(),
            )
        )
    )
    val uiState = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<CalendarUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        viewModelScope.launch {
            monthlyReviews.collect(::updateMonthlyReviews)
        }
    }

    private fun updateMonthlyReviews(reviews: List<Review>) {
        _uiState.update { state ->
            state.copy(
                monthlyReviews = reviews,
                calendarState = state.calendarState.copy(
                    cellStatesByDate = reviews.toCalendarCellStates()
                )
            )
        }
    }

    private fun List<Review>.toCalendarCellStates(): Map<LocalDate, CalendarCellUiState> =
        groupBy { it.createdAt.toLocalDate() }
            .mapValues { (date, reviews) ->
                CalendarCellUiState(
                    date = date,
                    reviewCount = reviews.size,
                    thumbnailUrl = reviews.firstNotNullOfOrNull { review -> review.images.firstOrNull()?.uri }
                )
            }

    fun onAction(action: CalendarUiAction) {
        when (action) {
            is CalendarUiAction.MonthChanged -> onMonthChange(action.month)
            is CalendarUiAction.DateClicked -> onDateClick(action.date)
            is CalendarUiAction.CreateReviewClicked -> onCreateReviewClick()
            is CalendarUiAction.ReviewClicked -> onReviewClick(action.reviewId)
            is CalendarUiAction.GoToTodayClicked -> onGoToTodayClick()
        }
    }

    private fun onMonthChange(month: YearMonth) {
        _uiState.update {
            it.copy(
                monthlyReviews = emptyList(),
                calendarState = CalendarContentUiState(displayedYearMonth = month)
            )
        }
        currentMonth.value = month
    }

    private fun onDateClick(date: LocalDate) {
        _uiState.update {
            it.copy(selectedDate = date)
        }
    }

    private fun onGoToTodayClick() {
        today = Clock.System.todayIn(TimeZone.currentSystemDefault())
        _uiState.update {
            it.copy(
                monthlyReviews = emptyList(),
                calendarState = CalendarContentUiState(
                    displayedYearMonth = today.toYearMonth(),
                )
            )
        }
        currentMonth.value = today.toYearMonth()
    }

    private fun onCreateReviewClick() {
        viewModelScope.launch {
            _uiEvent.emit(
                CalendarUiEvent.NavigateToCreate(
                    _uiState.value.selectedDate.toString()
                )
            )
        }
    }

    private fun onReviewClick(reviewId: Long) {
        viewModelScope.launch {
            _uiEvent.emit(CalendarUiEvent.NavigateToDetail(reviewId))
        }
    }
}
