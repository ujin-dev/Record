package com.yujin.record.calendar.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.yujin.record.calendar.CalendarContentUiState
import com.yujin.record.calendar.CalendarUiAction
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.TimeZone
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlinx.datetime.todayIn
import kotlin.time.Clock

private const val RANGE = 1_200 // 100 year
private const val BASE_PAGE = RANGE / 2 // for infinite swipe

internal fun betweenMonth(start: YearMonth, end: YearMonth) =
    (end.year - start.year) * 12 + (end.month.number - start.month.number)

@Composable
fun CalendarPager(
    state: CalendarContentUiState,
    selectedDate: LocalDate,
    onAction: (CalendarUiAction) -> Unit,
    goToTodayRequestId: Int,
    modifier: Modifier = Modifier,
) {
    val anchorYearMonth = remember { state.displayedYearMonth }
    val currentState by rememberUpdatedState(state)
    val pagerState = rememberPagerState(
        initialPage = BASE_PAGE,
        pageCount = { RANGE },
    )

    LaunchedEffect(state.displayedYearMonth) {
        val targetPage = BASE_PAGE + betweenMonth(anchorYearMonth, state.displayedYearMonth)
        if (pagerState.currentPage != targetPage) {
            pagerState.scrollToPage(targetPage)
        }
    }

    LaunchedEffect(goToTodayRequestId) {
        if (goToTodayRequestId > 0) {
            val today = Clock.System.todayIn(TimeZone.currentSystemDefault())
            val todayMonth = YearMonth(today.year, today.month)
            val todayPage = BASE_PAGE + betweenMonth(anchorYearMonth, todayMonth)
            pagerState.animateScrollToPage(todayPage)
        }
    }

    LaunchedEffect(pagerState) {
        snapshotFlow { pagerState.currentPage }
            .map { page ->
                anchorYearMonth.plus(page - BASE_PAGE, DateTimeUnit.MONTH)
            }.collect { targetMonth ->
                if (targetMonth != currentState.displayedYearMonth) {
                    onAction(CalendarUiAction.MonthChanged(targetMonth))
                }
            }
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier,
        beyondViewportPageCount = 11,
        verticalAlignment = Alignment.Top,
    ) { page ->
        val month = anchorYearMonth.plus(page - BASE_PAGE, DateTimeUnit.MONTH)

        CalendarGrid(
            month = month,
            state = if (state.displayedYearMonth == month) state.cellStatesByDate else null,
            selectedDate = selectedDate,
            onAction = onAction,
        )
    }
}
