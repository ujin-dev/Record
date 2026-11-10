package com.yujin.record.presentation.reviewform.component

import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
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
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(
    rating: Float,
    onRatingChange: (Float) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .pointerInput(Unit) {
                detectTapGestures { point ->
                    val starWidth = size.width / 5f
                    val index = (point.x / starWidth).toInt().coerceIn(0, 4)
                    val fraction = (point.x - index * starWidth) / starWidth
                    onRatingChange(index + if (fraction < 0.5f) 0.5f else 1f)
                }
            }
            .pointerInput(Unit) {
                detectHorizontalDragGestures { change: PointerInputChange, _ ->
                    val starWidth = size.width / 5f
                    val index = (change.position.x / starWidth).toInt().coerceIn(0, 4)
                    val fraction = (change.position.x - index * starWidth) / starWidth
                    onRatingChange(index + if (fraction < 0.5f) 0.5f else 1f)
                    change.consume()
                }
            },
    ) {
        repeat(5) { index ->
            Icon(
                imageVector = when {
                    index + 1 <= rating -> Icons.Filled.Star
                    index < rating -> Icons.AutoMirrored.Filled.StarHalf
                    else -> Icons.Filled.StarBorder
                },
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(32.dp),
            )
        }
    }
}
