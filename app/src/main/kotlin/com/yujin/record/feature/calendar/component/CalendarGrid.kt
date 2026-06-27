package com.yujin.record.feature.calendar.component

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yujin.record.feature.calendar.CalendarDayUiState
import kotlinx.datetime.LocalDate

@Composable
fun CalendarGrid(
    modifier: Modifier = Modifier,
    days: List<CalendarDayUiState>,
    onDateClick: (LocalDate) -> Unit
) {
    Column(modifier = modifier) {
        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                week.forEach { day ->
                    CalendarCell(
                        modifier = Modifier
                            .weight(1f)
                            .aspectRatio(1f / 1.4f),
                        day = day.date,
                        onClick = {
                            onDateClick(day.date)
                        },
                    )
                }
            }
        }
    }
}