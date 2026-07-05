package com.yujin.record.feature.review.detail

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

@Composable
fun ReviewDetailRoute(
    onBackClick: () -> Unit,
    viewModel: ReviewDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ReviewDetailScreen(
        uiState = uiState,
        onBackClick,
    )
}
