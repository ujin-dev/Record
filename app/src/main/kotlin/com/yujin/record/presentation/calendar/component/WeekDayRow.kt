package com.yujin.record.presentation.calendar.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringArrayResource
import com.yujin.record.R

@Composable
fun WeekDayRow() {
    val days = stringArrayResource(R.array.weekday_labels)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(30.dp)
    ) {
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelMedium,
            )
        }
    }
}
