package com.yujin.record.reviewdetail

import android.annotation.SuppressLint
import android.app.Activity
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import coil3.compose.AsyncImage
import com.yujin.record.model.ReviewType
import com.yujin.record.reviewdetail.component.TopAppBarIconButton
import com.yujin.record.ui.MEDIA_COVER_ASPECT_RATIO
import com.yujin.record.ui.toDisplayString

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReviewDetailScreen(
    uiState: ReviewDetailScreenState,
    onAction: (ReviewDetailUiAction) -> Unit,
) {

    val view = LocalView.current
    val window = (view.context as Activity).window

    DisposableEffect(window) {
        val controller = WindowCompat.getInsetsController(window, view)
        val previousAppearance = controller.isAppearanceLightStatusBars

        controller.isAppearanceLightStatusBars = false

        onDispose {
            controller.isAppearanceLightStatusBars = previousAppearance
        }
    }

    Box {
        when (uiState) {
            ReviewDetailScreenState.Loading -> LoadingScreen()
            ReviewDetailScreenState.NotFound -> NotFoundScreen()
            is ReviewDetailScreenState.Success -> SuccessScreen(
                uiState = uiState,
            )
        }

        TopAppBar(
            title = {},
            modifier = Modifier
                .align(Alignment.TopCenter)
                .fillMaxWidth(),
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent,
                scrolledContainerColor = Color.Transparent
            ),
            navigationIcon = {
                TopAppBarIconButton(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                    contentDescription = "뒤로가기",
                    onClick = {
                        onAction(ReviewDetailUiAction.BackClicked)
                    }
                )
            },
            actions = {
                if (uiState is ReviewDetailScreenState.Success) {
                    TopAppBarIconButton(
                        imageVector = Icons.Default.Edit,
                        contentDescription = "수정하기",
                        onClick = {
                            onAction(ReviewDetailUiAction.EditClicked)
                        }
                    )

                    TopAppBarIconButton(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "삭제하기",
                        onClick = {
                            onAction(ReviewDetailUiAction.DeleteClicked)
                        }
                    )
                }
            },
        )
    }
}

@Composable
fun LoadingScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CircularProgressIndicator(
            modifier = Modifier
                .size(64.dp),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.surfaceVariant,
        )
    }
}

@Composable
fun NotFoundScreen() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        Text(text = "not found review")
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuccessScreen(
    uiState: ReviewDetailScreenState.Success,
) {
    val thumbUri = remember { mutableStateOf(uiState.review.images.firstOrNull()?.uri) }

    BoxWithConstraints {
        BottomSheetScaffold(
            sheetContent = {
                ContentView(
                    uiState = uiState,
                    onClicked = { uri -> thumbUri.value = uri }
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

@Composable
fun ContentView(
    uiState: ReviewDetailScreenState.Success,
    onClicked: (Uri) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = Modifier
            .fillMaxWidth()
            .verticalScroll(rememberScrollState())
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
    ) {
        if (uiState.review.images.size > 1) {
            // images
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = 6.dp,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                uiState.review.images.forEach { image ->
                    AsyncImage(
                        model = image.uri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(
                                enabled = true,
                                onClick = { onClicked(image.uri) }
                            ),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            // title
            Text(
                text = uiState.review.title,
                fontSize = 23.sp,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineLarge,
            )

            if (uiState.review.creatorName.isNotEmpty()) {
                Text(
                    text = uiState.review.creatorName,
                    style = MaterialTheme.typography.bodyLarge,
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // review type
                Icon(
                    imageVector =
                        when (uiState.review.type) {
                            ReviewType.MOVIE -> Icons.Filled.Movie
                            ReviewType.TV_SERIES -> Icons.Filled.LiveTv
                            ReviewType.BOOK -> Icons.AutoMirrored.Filled.MenuBook
                        },
                    contentDescription = "review type icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = uiState.review.type.displayName,
                    style = MaterialTheme.typography.bodyLarge,
                    modifier = Modifier.offset(y = (-1.5).dp)
                )

                Text(text = " · ", fontWeight = FontWeight.ExtraBold)

                // rating
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = "rating icon",
                    modifier = Modifier.size(20.dp)
                )
                Text(
                    text = uiState.review.rating.toString(),
                    style = MaterialTheme.typography.bodyLarge,
                )
            }

            // created at
            Text(
                text = uiState.review.createdAt.toDisplayString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodySmall,
            )
        }

        // tags
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            uiState.review.tags.forEach { tag ->
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    SuggestionChip(
                        onClick = { /*TODO*/ },
                        label = { Text(text = tag) },
                    )
                }
            }
        }

        // content
        Text(
            text = uiState.review.content,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
