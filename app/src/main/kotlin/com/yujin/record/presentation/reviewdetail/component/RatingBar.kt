package com.yujin.record.presentation.reviewdetail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Float,
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy((-2.5).dp),
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = when {
                    index + 1 <= rating -> Icons.Filled.Star
                    index < rating -> Icons.AutoMirrored.Filled.StarHalf
                    else -> Icons.Filled.StarBorder
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.size(16.dp)
            )
        }
    }
}
