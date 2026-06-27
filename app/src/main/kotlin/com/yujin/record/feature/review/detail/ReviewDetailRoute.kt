package com.yujin.record.feature.review.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yujin.record.core.data.repository.ReviewRepository

@Composable
fun ReviewDetailRoute(reviewId: Long,
                      onBackClick: () -> Unit,
                      reviewRepository: ReviewRepository,
) {
    val viewModel: ReviewDetailViewModel = viewModel(
        // todo : Integrate Hilt
        key = "review_detail_$reviewId",
        factory = ReviewDetailViewModel.Factory(reviewId, reviewRepository)
    )
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewDetailScreen(
        uiState = uiState,
        onBackClick,
    )
}
