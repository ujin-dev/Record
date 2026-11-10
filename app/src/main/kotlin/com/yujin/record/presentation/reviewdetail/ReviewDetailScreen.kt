package com.yujin.record.presentation.reviewdetail

import android.annotation.SuppressLint
import android.app.Activity
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat
import com.yujin.record.presentation.reviewdetail.component.ErrorContent
import com.yujin.record.presentation.reviewdetail.component.LoadingContent
import com.yujin.record.presentation.reviewdetail.component.ReviewContent
import com.yujin.record.presentation.reviewdetail.component.ReviewDetailTopAppBar
import com.yujin.record.presentation.reviewdetail.component.SuccessContent
import com.yujin.record.ui.theme.Dimens

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReviewDetailScreen(
    uiState: ReviewDetailScreenState,
    onAction: (ReviewDetailUiAction) -> Unit,
) {

    val view = LocalView.current
    val window = (view.context as Activity).window

    DisposableEffect(window, uiState) {
        val controller = WindowCompat.getInsetsController(window, view)
        val previousAppearance = controller.isAppearanceLightStatusBars

        if (uiState is ReviewDetailScreenState.Success && uiState.review.images.isNotEmpty()) {
            controller.isAppearanceLightStatusBars = false
        }

        onDispose {
            controller.isAppearanceLightStatusBars = previousAppearance
        }
    }

    Box {
        when (uiState) {
            ReviewDetailScreenState.Loading -> LoadingContent()
            ReviewDetailScreenState.NotFound -> ErrorContent()
            is ReviewDetailScreenState.Success ->
                if (uiState.review.images.isNotEmpty()) {
                    SuccessContent(uiState = uiState)
                } else {
                    val density = LocalDensity.current
                    val statusBarHeight = with(density) {
                        WindowInsets.statusBars.getTop(this).toDp()
                    }
                    val topAppBarHeight = TopAppBarDefaults.TopAppBarExpandedHeight
                    val totalHeight = statusBarHeight + topAppBarHeight
                    ReviewContent(
                        uiState = uiState,
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(
                                start = Dimens.screenPadding,
                                top = totalHeight,
                                end = Dimens.screenPadding,
                                bottom = Dimens.screenPadding
                            )
                    )
                }
        }

        ReviewDetailTopAppBar(
            showActions = uiState is ReviewDetailScreenState.Success,
            backClick = { onAction(ReviewDetailUiAction.BackClicked) },
            editClick = { onAction(ReviewDetailUiAction.EditClicked) },
            deleteClick = { onAction(ReviewDetailUiAction.DeleteClicked) }
        )
    }
}
