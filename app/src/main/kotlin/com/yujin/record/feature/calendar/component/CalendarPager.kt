package com.yujin.record.feature.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import com.yujin.record.feature.calendar.CalendarCellUiState
import com.yujin.record.feature.calendar.CalendarState
import com.yujin.record.feature.calendar.CalendarUiAction
import kotlinx.coroutines.flow.map
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.YearMonth
import kotlinx.datetime.number
import kotlinx.datetime.plus

private const val RANGE = 1_200 // 100 year
private const val BASE_PAGE = RANGE / 2 // for infinite swipe

internal fun betweenMonth(start: YearMonth, end: YearMonth) = (end.year - start.year) * 12 +
        (end.month.number - start.month.number)

@Composable
fun CalendarPager(
    state: CalendarState,
    onAction: (CalendarUiAction) -> Unit,
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

    HorizontalPager(state = pagerState) { page ->
        val weeks = remember(state.calendarDays) {
            state.calendarDays.chunked(7)
        }

        Column {
            weeks.forEach { week ->
                Row(modifier = Modifier.fillMaxWidth()) {
                    week.forEach { day ->
                        val cellModifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f / 1.4f)

                        when (day) {
                            is CalendarCellUiState.Active -> {
                                CalendarCell(
                                    modifier = cellModifier,
                                    state = day,
                                    selected = day.date == state.selectedDate,
                                    onClick = { onAction(CalendarUiAction.DateClicked(day.date)) },
                                )
                            }

                            CalendarCellUiState.Empty -> {
                                Box(modifier = cellModifier)
                            }
                        }
                    }
                }
            }
        }
    }
}
