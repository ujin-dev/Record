package com.yujin.record.feature.calendar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import com.yujin.record.feature.calendar.CalendarCellUiState

@Composable
fun CalendarCell(
    modifier: Modifier = Modifier,
    state: CalendarCellUiState.Active,
    selected: Boolean = false,
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier.clickable(onClick = onClick)) {
        // todo state.thumbnailUrl

        Text(
            text = state.date.day.toString(),
            modifier = Modifier.align(Center),
            fontWeight = if (selected) FontWeight.ExtraBold else FontWeight.Normal,
        )

        if (state.reviewCount > 0) {
            Text(text = "${state.reviewCount}", modifier = Modifier.align(BottomEnd))
        }
    }
}