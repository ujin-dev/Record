package com.yujin.record.presentation.reviewdetail.component

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.presentation.reviewdetail.ReviewDetailScreenState
import com.yujin.record.presentation.toDisplayString

@Composable
fun ReviewContent(
    modifier: Modifier = Modifier,
    uiState: ReviewDetailScreenState.Success,
    onClicked: ((Uri) -> Unit)? = null,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
    ) {
        if (uiState.review.images.size > 1 && onClicked != null) {
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
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineLarge,
            )

            if (uiState.review.creatorName.isNotEmpty()) {
                Text(
                    text = uiState.review.creatorName,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                    ),
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
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                    ),
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
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontSize = 18.sp,
                    ),
                )
            }

            // created at
            Text(
                text = uiState.review.createdAt.toDisplayString(),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge,
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
            style = MaterialTheme.typography.bodyLarge.copy(
                fontSize = 20.sp,
            ),
        )
    }
}