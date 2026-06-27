package com.yujin.record.feature.calendar.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.yujin.record.feature.calendar.CalendarCellUiState
import kotlinx.datetime.LocalDate

@Composable
fun CalendarGrid(
    modifier: Modifier = Modifier,
    selectedDate: LocalDate,
    days: List<CalendarCellUiState>,
    onDateClick: (LocalDate) -> Unit
) {
    Column(modifier = modifier) {
        val cellModifier = Modifier
            .weight(1f)
            .aspectRatio(1f / 1.4f)

        days.chunked(7).forEach { week ->
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                week.forEach { day ->
                    when (day) {
                        is CalendarCellUiState.Active -> {
                            CalendarCell(
                                modifier = cellModifier,
                                state = day,
                                selected = day == selectedDate,
                                onClick = {
                                    onDateClick(day.date)
                                },
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