package com.yujin.record.feature.review.form

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
                is ReviewFormUiEvent.ShowToast -> {} // TODO: Show toast
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewFormScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onAction = viewModel::onAction,
    )
}