package com.yujin.record.presentation.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun CalendarRoute(
    onNavigateToCreate: (String) -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: CalendarViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is CalendarUiEvent.NavigateToCreate -> onNavigateToCreate(event.createdDate)
                is CalendarUiEvent.NavigateToDetail -> onNavigateToDetail(event.reviewId)
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    CalendarScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}