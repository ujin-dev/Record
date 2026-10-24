package com.yujin.record.feature.calendar

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yujin.record.core.data.mapper.toReview
import com.yujin.record.core.data.repository.ReviewRepository
import com.yujin.record.core.model.Review
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.toLocalDateTime
import javax.inject.Inject
import kotlin.time.Clock

@HiltViewModel
class CalendarViewModel @Inject constructor(
    private val reviewRepository: ReviewRepository
) : ViewModel() {
    private val selectedDate: MutableStateFlow<LocalDate> = MutableStateFlow(
        Clock.System.now()
            .toLocalDateTime(TimeZone.currentSystemDefault())
            .date
    )

    private val currentMonth: MutableStateFlow<YearMonth> =
        MutableStateFlow(selectedDate.value.toYearMonth())

    @OptIn(ExperimentalCoroutinesApi::class)
    private val monthlyReviews: Flow<List<Review>> =
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
            monthlyReviews,
        ) { month, date, reviews ->
            CalendarUiState(
                selectedDate = date,
                calendarState = CalendarContentUiState(
                    displayedYearMonth = month,
                    cellStatesByDate = reviews
                        .groupBy { it.createdAt.toLocalDate() }
                        .mapValues { (date, reviews) ->
                            CalendarCellUiState(
                                date = date,
                                reviewCount = reviews.size,
                                thumbnailUrl = reviews.lastOrNull()?.images?.firstOrNull()?.uri
                            )
                        }
                ),
                monthlyReviews = reviews
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = CalendarUiState(
                selectedDate = selectedDate.value,
                calendarState = CalendarContentUiState(
                    displayedYearMonth = currentMonth.value,
                )
            )
        )

    private val _uiEvent: MutableSharedFlow<CalendarUiEvent> = MutableSharedFlow()
    val uiEvent: SharedFlow<CalendarUiEvent> = _uiEvent.asSharedFlow()

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
        selectedDate.value = date
    }

    fun onCreateReviewClick() {
        viewModelScope.launch {
            _uiEvent.emit(
                CalendarUiEvent.NavigateToCreate(selectedDate.value.toString())
            )
        }
    }

    fun onReviewClick(reviewId: Long) {
        viewModelScope.launch {
            _uiEvent.emit(CalendarUiEvent.NavigateToDetail(reviewId))
        }
    }
}