package com.yujin.record.feature.calendar.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yujin.record.core.ui.MEDIA_COVER_ASPECT_RATIO
import com.yujin.record.feature.calendar.CalendarCellUiState
import com.yujin.record.feature.calendar.CalendarUiAction
import com.yujin.record.feature.calendar.firstDay
import com.yujin.record.feature.calendar.toYearMonth
import kotlinx.datetime.DatePeriod
import kotlinx.datetime.LocalDate
import kotlinx.datetime.YearMonth
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.minus
import kotlinx.datetime.plus
import kotlin.math.ceil

@Composable
fun CalendarGrid(
    month: YearMonth,
    state: Map<LocalDate, CalendarCellUiState>? = null,
    selectedDate : LocalDate,
    onAction: (CalendarUiAction) -> Unit,
) {
    val firstDay = month.firstDay()
    val startOffset = firstDay.dayOfWeek.isoDayNumber % 7
    val weekCount = ceil((startOffset + month.numberOfDays) / 7f).toInt()
    val totalCellCount = weekCount * 7
    val startDate = firstDay - DatePeriod(days = startOffset)

    val dates: List<LocalDate?> = (0 until totalCellCount).map { index ->
        val date = startDate + DatePeriod(days = index)
        if (date.toYearMonth() == month) date else null
    }

    val weeks = remember(dates) {
        dates.chunked(7)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight(Alignment.Top),
        verticalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        weeks.forEach { week ->
            Row(
                modifier = Modifier.padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                week.forEach { localDate ->
                    if (localDate == null) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(MEDIA_COVER_ASPECT_RATIO)
                        )
                    } else {
                        val cellState = state?.get(localDate)

                        DateCell(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(MEDIA_COVER_ASPECT_RATIO),
                            date = localDate.day,
                            isSelected = localDate == selectedDate,
                            thumbnailUri = cellState?.thumbnailUrl,
                            reviewCount = cellState?.reviewCount ?: 0,
                            onClick = { onAction(CalendarUiAction.DateClicked(localDate)) },
                        )
                    }
                }
            }
        }
    }
}