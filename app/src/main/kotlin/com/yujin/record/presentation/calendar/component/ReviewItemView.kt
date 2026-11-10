package com.yujin.record.presentation.calendar.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yujin.record.domain.model.Review
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.ui.theme.Dimens

@Composable
fun ReviewItemView(review: Review, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = onClick)
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(Dimens.imageCornerSize)),
            contentAlignment = Alignment.Center,
        ) {
            review.images.firstOrNull()?.let { image ->
                AsyncImage(
                    model = image.uri,
                    contentDescription = "thumbnail",
                    contentScale = ContentScale.Crop,
                )
            } ?: run {
                val icon = when (review.reviewType) {
                    ReviewType.MOVIE -> Icons.Outlined.Movie
                    ReviewType.TV_SERIES -> Icons.Outlined.LiveTv
                    ReviewType.BOOK -> Icons.AutoMirrored.Outlined.MenuBook
                }
                Icon(
                    imageVector = icon,
                    contentDescription = "review type",
                    modifier = Modifier.size(24.dp)
                )
            }
        }

        Spacer(Modifier.width(12.dp))

        Column {
            Text(
                text = review.title,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                val rate = review.rating
                repeat(5) { index ->
                    Icon(
                        imageVector = when {
                            index + 1 <= rate -> Icons.Filled.Star
                            index < rate -> Icons.AutoMirrored.Filled.StarHalf
                            else -> Icons.Filled.StarBorder
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.size(16.dp),
                    )
                }
                Text(
                    text = "$rate",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}
