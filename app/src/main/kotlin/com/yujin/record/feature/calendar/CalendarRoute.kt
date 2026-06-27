package com.yujin.record.feature.calendar

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yujin.record.core.data.repository.ReviewRepository

@Composable
fun CalendarRoute(
    onNavigateToCreate: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    reviewRepository: ReviewRepository,
    viewModel: CalendarViewModel = viewModel(
        factory = CalendarViewModel.factory(reviewRepository)
    )
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is CalendarUiEvent.NavigateToCreate -> onNavigateToCreate()
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