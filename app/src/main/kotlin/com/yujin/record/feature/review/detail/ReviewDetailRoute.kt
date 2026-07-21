package com.yujin.record.feature.review.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReviewDetailRoute(
    onEditClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    onDelete: () -> Unit,
    viewModel: ReviewDetailViewModel = hiltViewModel(),
) {
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ReviewDetailUiEvent.NavigateToBack -> onBackClick()
                ReviewDetailUiEvent.NavigateToHome -> onDelete()
                is ReviewDetailUiEvent.NavigateToForm -> onEditClick(event.id)
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewDetailScreen(
        uiState = uiState,
        onAction = viewModel::onAction
    )
}
