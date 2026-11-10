package com.yujin.record.presentation.reviewdetail.component

import android.net.Uri
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
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
import com.yujin.record.presentation.MAX_IMAGE_COUNT
import com.yujin.record.presentation.reviewdetail.ReviewDetailScreenState
import com.yujin.record.presentation.toMonthDateString
import com.yujin.record.presentation.toYearString
import com.yujin.record.ui.theme.Dimens

@Composable
fun SuccessContent(
    modifier: Modifier = Modifier,
    uiState: ReviewDetailScreenState.Success,
    imageClick: ((Uri) -> Unit) = {},
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(15.dp),
        modifier = modifier
    ) {
        // images
        BoxWithConstraints(Modifier.fillMaxWidth()) {
            val spacing = Dimens.cardBetweenPadding
            val slotWidth = (maxWidth - spacing * (MAX_IMAGE_COUNT - 1)) / MAX_IMAGE_COUNT

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(
                    space = Dimens.cardBetweenPadding,
                    alignment = Alignment.CenterHorizontally
                )
            ) {
                uiState.review.images.forEach { image ->
                    AsyncImage(
                        model = image.uri,
                        contentDescription = null,
                        modifier = Modifier
                            .size(slotWidth)
                            .clip(RoundedCornerShape(Dimens.imageCornerSize))
                            .clickable(
                                enabled = true,
                                onClick = { imageClick(image.uri) }
                            ),
                        contentScale = ContentScale.Crop,
                    )
                }
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                // review type
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector =
                            when (uiState.review.type) {
                                ReviewType.MOVIE -> Icons.Filled.Movie
                                ReviewType.TV_SERIES -> Icons.Filled.LiveTv
                                ReviewType.BOOK -> Icons.AutoMirrored.Filled.MenuBook
                            },
                        contentDescription = "review type",
                        modifier = Modifier.size(32.dp),
                        tint = MaterialTheme.colorScheme.secondary,
                    )
                    Text(
                        text = uiState.review.type.displayName,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }

                VerticalDivider(
                    modifier = Modifier.height(40.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                // rating
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.review.rating.toString(),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    RatingBar(uiState.review.rating)
                }

                VerticalDivider(
                    modifier = Modifier.height(40.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.outlineVariant,
                )

                // created at
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.review.createdAt.toYearString(),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                    Text(
                        text = uiState.review.createdAt.toMonthDateString(),
                        color = MaterialTheme.colorScheme.secondary,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.secondary,
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                        ),
                    )
                }
            }
        }

        Column(modifier = Modifier.fillMaxWidth()) {
            // title
            Text(
                text = uiState.review.title,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.headlineLarge.copy(
                    color = MaterialTheme.colorScheme.primary,
                ),
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

        // tags
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(2.dp),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            uiState.review.tags.forEach { tag ->
                CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                    SuggestionChip(
                        onClick = { /*TODO image viewer*/ },
                        label = { Text(text = tag) },
                    )
                }
            }
        }

        // content
        Text(
            text = uiState.review.content,
            style = MaterialTheme.typography.bodyLarge.copy(
                color = MaterialTheme.colorScheme.primary,
                fontSize = 20.sp,
            ),
            lineHeight = 30.sp,
        )
    }
}