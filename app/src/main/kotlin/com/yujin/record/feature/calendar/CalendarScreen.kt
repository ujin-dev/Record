package com.yujin.record.feature.calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yujin.record.core.ui.preview.DefaultScreenPreviews
import com.yujin.record.core.ui.theme.RecordTheme
import com.yujin.record.feature.calendar.component.CalendarGrid
import com.yujin.record.feature.calendar.component.WeekDayRow
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    modifier: Modifier = Modifier,
    viewModel: CalendarViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(title = { Text(text = uiState.month.format(DateTimeFormatter.ofPattern("yyyy.MM"))) })
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = viewModel::onCreateReviewClick,
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
            CalendarGrid(
                days = uiState.calendarDays,
                onDateClick = viewModel::onDateClick
            )
        }
    }
}

@DefaultScreenPreviews
@Composable
fun CalendarScreenPreview() {
    RecordTheme {
        CalendarScreen()
    }
}