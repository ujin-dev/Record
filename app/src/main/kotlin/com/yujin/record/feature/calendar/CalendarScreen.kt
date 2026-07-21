package com.yujin.record.feature.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yujin.record.feature.calendar.component.CalendarPager
import com.yujin.record.feature.calendar.component.WeekDayRow

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onAction: (CalendarUiAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = {
                Text(text = uiState.calendarState.displayedYearMonth.toDisplayString())
            })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onAction(CalendarUiAction.CreateReviewClicked) },
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxWidth()
        ) {
            WeekDayRow()

            CalendarPager(
                state = uiState.calendarState,
                onAction = onAction,
            )

            HorizontalDivider()

            LazyColumn {
                items(uiState.reviews) { review ->
                    ListItem(
                        modifier = Modifier.clickable(onClick = {
                            onAction(
                                CalendarUiAction.ReviewClicked(
                                    review.id
                                )
                            )
                        }),
                        headlineContent = { Text(review.title) },
                        supportingContent = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp),
                                )
                                Text(text = review.rating.toString())
                            }
                        },
                    )
                }
            }
        }
    }
}