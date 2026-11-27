package com.yujin.record.presentation.reviewform

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalResources
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReviewFormRoute(
    onBackClick: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: ReviewFormViewModel = hiltViewModel()
) {
    val resources = LocalResources.current
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                is ReviewFormUiEvent.NavigateToDetail -> onNavigateToDetail(event.id)
                is ReviewFormUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(
                    resources.getString(event.messageRes)
                )
                ReviewFormUiEvent.NavigateToBack -> onBackClick()
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewFormScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState,
    )
}