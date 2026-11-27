package com.yujin.record.presentation.reviewdetail

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.util.trace
import com.yujin.record.presentation.reviewdetail.component.ErrorContent
import com.yujin.record.presentation.reviewdetail.component.LoadingContent
import com.yujin.record.presentation.reviewdetail.component.ReviewDetailTopAppBar
import com.yujin.record.presentation.reviewdetail.component.SuccessContent
import com.yujin.record.ui.theme.Dimens

@Composable
fun ReviewDetailScreen(
    uiState: ReviewDetailScreenState,
    onAction: (ReviewDetailUiAction) -> Unit,
    snackbarHostState: SnackbarHostState,
) = trace("ReviewDetailScreen") {
    Scaffold(
        topBar = {
            ReviewDetailTopAppBar(
                backClick = { onAction(ReviewDetailUiAction.BackClicked) },
                editClick = { onAction(ReviewDetailUiAction.EditClicked) },
                deleteClick = { onAction(ReviewDetailUiAction.DeleteClicked) },
                showActions = uiState is ReviewDetailScreenState.Success,
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        when (uiState) {
            ReviewDetailScreenState.Loading -> LoadingContent()
            ReviewDetailScreenState.NotFound -> ErrorContent()
            is ReviewDetailScreenState.Success -> SuccessContent(
                uiState = uiState,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(innerPadding)
                    .padding(
                        start = Dimens.screenPadding,
                        end = Dimens.screenPadding,
                        bottom = Dimens.screenPadding
                    )
            )
        }
    }
}
