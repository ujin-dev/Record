package com.yujin.record.calendar

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.outlined.MenuBook
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.material3.rememberStandardBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yujin.record.calendar.component.CalendarPager
import com.yujin.record.calendar.component.WeekDayRow
import com.yujin.record.model.Review
import com.yujin.record.model.ReviewType
import com.yujin.record.ui.theme.Dimens

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onAction: (CalendarUiAction) -> Unit,
) {
    val scaffoldState = rememberBottomSheetScaffoldState(
        bottomSheetState = rememberStandardBottomSheetState(
            initialValue = SheetValue.PartiallyExpanded
        )
    )
    val handlerHeight = 48.dp
    val minContentHeight = 72.dp
    val sheetPeekHeight = handlerHeight + minContentHeight * 1.5f

    Column(
        Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.navigationBars)
            .clipToBounds()
    ) {
        TopAppBar(
            title = {
                Text(
                    text = uiState.calendarState.displayedYearMonth.toDisplayString(),
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.SemiBold,
                )
            },
        )

        BottomSheetScaffold(
            sheetContent = {
                if (uiState.monthlyReviews.isEmpty()) {
                    Column(
                        modifier = Modifier.fillMaxWidth().height(minContentHeight * 1.5f),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.PostAdd,
                            contentDescription = "empty review",
                            modifier = Modifier.size(24.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                        Text(
                            text = "이 달의 리뷰를 작성해 보세요.",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.titleMedium,
                        )
                    }
                } else {
                    LazyColumn(Modifier.fillMaxHeight()) {
                        items(uiState.monthlyReviews) { review ->
                            ListItemView(review, onAction)
                        }
                    }
                }
            },
            modifier = Modifier.weight(1f),
            scaffoldState = scaffoldState,
            sheetPeekHeight = sheetPeekHeight,
        ) { innerPadding ->
            Column(
                Modifier
                    .padding(innerPadding)
                    .fillMaxWidth()
            ) {
                WeekDayRow()

                CalendarPager(
                    state = uiState.calendarState,
                    selectedDate = uiState.selectedDate,
                    onAction = onAction,
                )
            }
        }
        Surface(
            shadowElevation = 3.dp,
            color = MaterialTheme.colorScheme.surface,
        ) {
            Button(
                onClick = { onAction(CalendarUiAction.CreateReviewClicked) },
                modifier = Modifier
                    .fillMaxWidth()
                    .windowInsetsPadding(WindowInsets.navigationBars)
                    .padding(Dimens.screenPadding)
                    .imePadding()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
            ) {
                Text(
                    text = "리뷰 작성",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}

@Composable
fun ListItemView(review: Review, onAction: (CalendarUiAction) -> Unit) {
    Row(
        modifier = Modifier
            .clickable(onClick = {
                onAction(CalendarUiAction.ReviewClicked(review.id))
            })
            .fillMaxWidth()
            .height(72.dp)
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.Start,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center,
        ) {
            review.images.firstOrNull()?.let { image ->
                AsyncImage(
                    model = review.images.getOrNull(0)?.uri,
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
