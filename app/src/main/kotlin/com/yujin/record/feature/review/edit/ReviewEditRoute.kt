package com.yujin.record.feature.review.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun ReviewEditRoute(
    reviewId: Long? = null,
    onBackClick: () -> Unit,
    viewModel: ReviewEditViewModel = viewModel()
) {
    LaunchedEffect(reviewId) {
        viewModel.initialize(reviewId)
    }

    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewEditScreen(
        uiState = uiState,
        onBackClick = onBackClick,
        onAction = viewModel::onAction,
    )
}