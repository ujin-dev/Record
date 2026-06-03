package com.yujin.record.feature.review.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yujin.record.core.data.repository.ReviewRepository

@Composable
fun ReviewDetailRoute(reviewId: Long, onBackClick: () -> Unit, reviewRepository: ReviewRepository,
                      viewModel: ReviewDetailViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    ReviewDetailScreen(
        uiState = uiState,
        onBackClick,
    )
}
