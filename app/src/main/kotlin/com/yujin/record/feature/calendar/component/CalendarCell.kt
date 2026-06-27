package com.yujin.record.feature.calendar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomEnd
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import kotlinx.datetime.LocalDate

@Composable
fun CalendarCell(
    modifier: Modifier = Modifier,
    day: LocalDate,
    reviewCount: Int = 0,
    imageResId: Int? = null,
    onClick: () -> Unit = {}
) {
    Box(modifier = modifier.clickable(onClick = onClick)) {
        imageResId?.let {
            ReviewThumbnail(resId = imageResId)
        }

        Text(text = day.day.toString(), modifier = Modifier.align(Center))

        if (reviewCount > 1) {
            Text(text = "+${reviewCount - 1}", modifier = Modifier.align(BottomEnd))
        }
    }
}