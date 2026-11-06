package com.yujin.record.reviewform.component

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yujin.record.model.MediaItem
import com.yujin.record.ui.MEDIA_COVER_ASPECT_RATIO

@Composable
fun SearchResultItemView(state: MediaItem, onClick: () -> Unit) {
    Column(modifier = Modifier.width(100.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MEDIA_COVER_ASPECT_RATIO)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp),
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            state.posterPath?.let {
                AsyncImage(
                    model = state.posterPath,
                    contentDescription = state.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(MEDIA_COVER_ASPECT_RATIO)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Outlined.Movie,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = state.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            textAlign = TextAlign.Center,
            maxLines = 3,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}