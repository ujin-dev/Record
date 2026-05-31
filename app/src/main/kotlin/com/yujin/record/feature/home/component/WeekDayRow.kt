package com.yujin.record.feature.home.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.yujin.record.core.ui.preview.DefaultViewPreviews
import com.yujin.record.core.ui.theme.RecordTheme

@Composable
fun WeekDayRow() {
    val days = listOf("일", "월", "화", "수", "목", "금", "토")
    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { days ->
            Text(
                text = days,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )
        }
    }
}

@DefaultViewPreviews
@Composable
fun DayRowPreview() {
    RecordTheme {
        WeekDayRow()
    }
}