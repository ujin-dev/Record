package com.yujin.record.feature.calendar.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.BottomCenter
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage

@Composable
fun CalendarCell(
    modifier: Modifier = Modifier,
    date: Int,
    thumbnailUri: Uri? = null,
    reviewCount: Int = 0,
    onClick: () -> Unit = {}
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surfaceContainer,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable(onClick = onClick)
    ) {
        if (thumbnailUri != null) {
            AsyncImage(
                model = thumbnailUri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = "$date",
                modifier = Modifier.align(Center),
                color = MaterialTheme.colorScheme.onSurface,
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelLarge
            )
        }

        if (reviewCount > 1) {
            Box(
                modifier = Modifier
                    .align(BottomCenter)
                    .offset(y = (-2).dp)
                    .size(20.dp)
                    .background(MaterialTheme.colorScheme.tertiaryContainer, CircleShape),
                contentAlignment = Center,
                content = {
                    Text(
                        text = "$reviewCount",
                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                        fontWeight = FontWeight.ExtraBold,
                        style = MaterialTheme.typography.labelSmall,
                    )
                }
            )
        }
    }
}
