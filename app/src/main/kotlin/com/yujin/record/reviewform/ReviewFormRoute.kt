package com.yujin.record.reviewform

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReviewFormRoute(
    onBackClick: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: ReviewFormViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ReviewFormUiEvent.NavigateToDetail -> onNavigateToDetail(event.id)
                ReviewFormUiEvent.NavigateToBack -> onBackClick()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewFormScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
    )
}