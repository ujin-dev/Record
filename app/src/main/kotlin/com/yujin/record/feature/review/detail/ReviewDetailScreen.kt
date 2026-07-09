package com.yujin.record.feature.review.detail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yujin.record.core.ui.toDisplayString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewDetailScreen(
    uiState: ReviewDetailScreenState,
    onAction: (ReviewDetailUiAction) -> Unit,
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "리뷰 상세") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ReviewDetailUiAction.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    if (uiState is ReviewDetailScreenState.Success) {
                        IconButton(
                            onClick = {
                                onAction(ReviewDetailUiAction.EditClicked)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "수정하기"
                            )
                        }
                        IconButton(
                            onClick = {
                                onAction(ReviewDetailUiAction.DeleteClicked)
                            },
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "삭제하기"
                            )
                        }
                    }
                },
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize(),
        ) {
            when (uiState) {
                ReviewDetailScreenState.Loading -> LoadingScreen()
                ReviewDetailScreenState.NotFound -> NotFoundScreen()
                is ReviewDetailScreenState.Success -> SuccessScreen(review = uiState.review)
            }
        }
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

@Composable
fun SuccessScreen(review: ReviewDetailUiState) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(text = review.createdAt.toDisplayString())

        Text(text = review.title, style = MaterialTheme.typography.titleLarge)

        Text(text = buildString {
            append(review.type.displayName)
            if (review.creatorName.isNotEmpty()) {
                append(" · ")
                append(review.creatorName)
            }
        })

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null
            )
            Text(text = review.rating.toString())
            Text(text = " · ")
            Text(text = review.shortReview)
        }

        Text(text = review.content, style = MaterialTheme.typography.titleMedium)
    }
}
