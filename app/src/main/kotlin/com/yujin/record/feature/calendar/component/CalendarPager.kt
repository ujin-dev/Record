package com.yujin.record.feature.calendar.component

import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import com.yujin.record.feature.calendar.CalendarContentUiState
import com.yujin.record.feature.calendar.CalendarUiAction
import com.yujin.record.feature.calendar.firstDay
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import kotlinx.datetime.plus
import kotlin.math.ceil

private const val RANGE = 1_200 // 100 year
private const val BASE_PAGE = RANGE / 2 // for infinite swipe

internal fun betweenMonth(start: YearMonth, end: YearMonth) = (end.year - start.year) * 12 +
        (end.month.number - start.month.number)

private fun getWeekCount(month: YearMonth): Int {
    val startOffset = month.firstDay().dayOfWeek.isoDayNumber % 7
    val daysInMonth = month.numberOfDays
    return ceil((startOffset + daysInMonth) / 7f).toInt()
}

@Composable
fun CalendarPager(
    state: CalendarContentUiState,
    selectedDate: LocalDate,
    onAction: (CalendarUiAction) -> Unit,
) {
    val anchorYearMonth = remember { state.displayedYearMonth }
    val currentState by rememberUpdatedState(state)
    val pagerState = rememberPagerState(
        initialPage = BASE_PAGE,
        pageCount = { RANGE },
    )

//    val weekCount = remember { mutableIntStateOf(getWeekCount(state.displayedYearMonth)) }

    LaunchedEffect(state.displayedYearMonth) {
        val targetPage = BASE_PAGE + betweenMonth(anchorYearMonth, state.displayedYearMonth)
        if (pagerState.currentPage != targetPage) {
            pagerState.scrollToPage(targetPage)
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

//    LaunchedEffect(pagerState) {
//        snapshotFlow { pagerState.settledPage }
//            .collect { page ->
//                val targetMonth = anchorYearMonth.plus(page - BASE_PAGE, DateTimeUnit.MONTH)
//                weekCount.intValue = getWeekCount(targetMonth)
//            }
//    }

    HorizontalPager(
        state = pagerState,
        beyondViewportPageCount = 3,
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
