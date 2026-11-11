package com.yujin.record.presentation.reviewdetail

import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReviewDetailRoute(
    onEditClick: (Long) -> Unit,
    onBackClick: () -> Unit,
    onDelete: () -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    viewModel: ReviewDetailViewModel = hiltViewModel(),
) {
    val resources = LocalContext.current.resources
    LaunchedEffect(Unit) {
        viewModel.uiEvent.collect { event ->
            when (event) {
                ReviewDetailUiEvent.NavigateToBack -> onBackClick()
                ReviewDetailUiEvent.NavigateToHome -> onDelete()
                is ReviewDetailUiEvent.NavigateToForm -> onEditClick(event.id)
                is ReviewDetailUiEvent.ShowSnackbar -> snackbarHostState.showSnackbar(
                    resources.getString(
                        event.messageRes
                    )
                )
            }
        }
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewDetailScreen(
        uiState = uiState,
        onAction = viewModel::onAction,
        snackbarHostState = snackbarHostState
    )
}
