package com.yujin.record.reviewdetail.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetValue
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yujin.record.reviewdetail.ReviewDetailScreenState
import com.yujin.record.ui.MEDIA_COVER_ASPECT_RATIO
import com.yujin.record.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessContent(
    uiState: ReviewDetailScreenState.Success,
) {
    val thumbUri = remember { mutableStateOf(uiState.review.images.firstOrNull()?.uri) }

    BoxWithConstraints {
        BottomSheetScaffold(
            sheetContent = {
                ReviewContent(
                    uiState = uiState,
                    onClicked = { uri -> thumbUri.value = uri },
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                        .padding(
                            start = Dimens.bottomSheetPadding,
                            end = Dimens.bottomSheetPadding,
                            bottom = Dimens.bottomSheetPadding
                        )
                )
            },
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.navigationBars),
            scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberStandardBottomSheetState(
                    initialValue = SheetValue.PartiallyExpanded
                )
            ),
            sheetPeekHeight = maxHeight - (maxHeight * MEDIA_COVER_ASPECT_RATIO),
        ) { _ ->
            Box {
                AsyncImage(
                    model = thumbUri.value,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(MEDIA_COVER_ASPECT_RATIO)
                        .align(Alignment.TopCenter),
                    contentScale = ContentScale.Crop
                )
                Box(
                    Modifier
                        .fillMaxWidth()
                        .height(100.dp)
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    Color.Black.copy(alpha = 0.65f),
                                    Color.Black.copy(alpha = 0.25f),
                                    Color.Transparent
                                )
                            )
                        )
                )
            }
        }
    }
}