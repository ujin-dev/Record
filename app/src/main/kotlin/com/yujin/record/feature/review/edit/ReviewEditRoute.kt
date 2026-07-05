package com.yujin.record.feature.review.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReviewEditRoute(
    onBackClick: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    viewModel: ReviewEditViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ReviewEditUiEvent.NavigateToDetail -> onNavigateToDetail(event.id)
                is ReviewEditUiEvent.ShowToast -> {} // TODO: Show toast
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewEditScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onAction = viewModel::onAction,
    )
}