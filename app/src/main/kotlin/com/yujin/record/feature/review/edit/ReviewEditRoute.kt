package com.yujin.record.feature.review.edit

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yujin.record.core.data.repository.ReviewRepository

@Composable
fun ReviewEditRoute(
    reviewId: Long? = null,
    onBackClick: () -> Unit,
    onNavigateToDetail: (Long) -> Unit,
    reviewRepository: ReviewRepository,
    viewModel: ReviewEditViewModel = viewModel(
        factory = ReviewEditViewModel.factory(reviewRepository)
    )
) {
    LaunchedEffect(reviewId) {
        viewModel.initialize(reviewId)
    }

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