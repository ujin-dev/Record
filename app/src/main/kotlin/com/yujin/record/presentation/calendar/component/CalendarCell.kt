package com.yujin.record.presentation.calendar.component

import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.GenericShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.util.trace
import coil3.compose.AsyncImage
import com.yujin.record.ui.theme.Dimens

@Composable
fun CalendarCell(
    modifier: Modifier = Modifier,
    date: Int,
    isSelected: Boolean,
    thumbnailUri: Uri? = null,
    reviewCount: Int = 0,
    onClick: () -> Unit = {}
) = trace("CalendarCell") {
    Box(
        modifier = modifier
            .clip(shape = RoundedCornerShape(Dimens.imageCornerSize))
            .clickable(onClick = onClick)
            .background(
                color =
                    if (reviewCount > 0) {
                        MaterialTheme.colorScheme.secondaryContainer
                    } else {
                        MaterialTheme.colorScheme.surfaceContainer
                    }
            )
    ) {
        if (thumbnailUri != null) {
            AsyncImage(
                model = thumbnailUri,
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
            )
        } else {
            Text(
                text = "$date",
                modifier = Modifier.align(Center),
                color = if (reviewCount > 0) {
                    MaterialTheme.colorScheme.onSecondaryContainer
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                fontWeight = FontWeight.ExtraBold,
                style = MaterialTheme.typography.labelLarge
            )
        }

        if (reviewCount > 1) {
            Box(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .size(width = 30.dp, height = 30.dp)
                    .clip(GenericShape { size, _ ->
                        moveTo(0f, size.height)
                        lineTo(size.width, 0f)
                        lineTo(size.width, size.height)
                        close()
                    })
                    .background(MaterialTheme.colorScheme.tertiaryContainer),
                contentAlignment = Center,
                content = {
                    Text(
                        text = "$reviewCount",
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(bottom = 2.dp, end = 4.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = MaterialTheme.colorScheme.onTertiaryContainer,
                            fontWeight = FontWeight.ExtraBold
                        ),
                    )
                }
            )
        }

        if (isSelected) {
            Box(
                modifier = Modifier
                    .padding(4.dp)
                    .size(9.dp)
                    .border(
                        width = 1.5.dp,
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = CircleShape
                    )
                    .background(
                        color = MaterialTheme.colorScheme.primary,
                        shape = CircleShape
                    )
                    .align(Alignment.TopEnd)
            )
        }
    }
}
