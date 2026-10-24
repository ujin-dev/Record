package com.yujin.record.feature.review.detail

import android.annotation.SuppressLint
import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yujin.record.core.ui.MEDIA_COVER_ASPECT_RATIO
import com.yujin.record.core.ui.toDisplayString

@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun ReviewDetailScreen(
    uiState: ReviewDetailScreenState,
    onAction: (ReviewDetailUiAction) -> Unit,
) {
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
                FilledIconButton(
                    onClick = { onAction(ReviewDetailUiAction.BackClicked) },
                    colors = IconButtonDefaults.iconButtonColors(
                        containerColor = Color.White.copy(alpha = 0.6f),
                        contentColor = MaterialTheme.colorScheme.primary
                    ),
                    shape = CircleShape,
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                        contentDescription = "뒤로가기"
                    )
                }
            },
            actions = {
                if (uiState is ReviewDetailScreenState.Success) {
                    FilledIconButton(
                        onClick = {
                            onAction(ReviewDetailUiAction.EditClicked)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.6f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "수정하기"
                        )
                    }
                    FilledIconButton(
                        onClick = {
                            onAction(ReviewDetailUiAction.DeleteClicked)
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Color.White.copy(alpha = 0.6f),
                            contentColor = MaterialTheme.colorScheme.primary
                        ),
                        shape = CircleShape,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "삭제하기"
                        )
                    }
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
    val sheetState = rememberStandardBottomSheetState(
        initialValue = SheetValue.Expanded
    )
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = sheetState
    )
    BoxWithConstraints(
        modifier = Modifier.fillMaxSize()
    ) {
        val width = maxWidth
        val height = maxHeight

        val thumbUri = remember { mutableStateOf(uiState.review.images.firstOrNull()?.uri) }
        val onClicked = { uri: Uri -> thumbUri.value = uri }

        BottomSheetScaffold(
            scaffoldState = scaffoldState,
            sheetPeekHeight = height - width / MEDIA_COVER_ASPECT_RATIO - 50.dp,
            modifier = Modifier
                .windowInsetsPadding(WindowInsets.navigationBars)
                .fillMaxSize(),
            sheetContent = { ContentView(uiState = uiState, onClicked = onClicked) }
        ) { _ ->
            AsyncImage(
                model = thumbUri.value,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(MEDIA_COVER_ASPECT_RATIO)
                    .align(Alignment.TopCenter),
                contentScale = ContentScale.Crop
            )
        }
    }
}

@Composable
fun ContentView(
    uiState: ReviewDetailScreenState.Success,
    onClicked: (Uri) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 32.dp, end = 32.dp, bottom = 32.dp)
    ) {
        if (uiState.review.images.size > 1) {
            Row(
                modifier = Modifier.height(100.dp),
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
                            .aspectRatio(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .clickable(enabled = true, onClick = {
                                onClicked(image.uri)
                            }),
                        contentScale = ContentScale.Crop,

                        )
                }
            }
            Spacer(Modifier.height(20.dp))
        }

        Text(
            text = uiState.review.title,
            fontSize = 23.sp,
            fontWeight = FontWeight.ExtraBold,
            style = MaterialTheme.typography.headlineLarge,
        )

        if (uiState.review.creatorName.isNotEmpty()) {
            Text(
                text = uiState.review.creatorName,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        Text(
            text = uiState.review.createdAt.toDisplayString(),
            style = MaterialTheme.typography.bodyMedium,
        )

        Spacer(Modifier.height(10.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(text = buildString {
                append(uiState.review.type.displayName)
                append(" · ")
            }, style = MaterialTheme.typography.bodyMedium)
            // rating
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null
            )
            Text(
                text = uiState.review.rating.toString(),
                style = MaterialTheme.typography.bodyMedium
            )
        }

        Spacer(Modifier.height(15.dp))

        Text(
            text = uiState.review.content,
            style = MaterialTheme.typography.bodyLarge,
        )
    }
}
