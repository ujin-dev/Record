package com.yujin.record.calendar

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.yujin.record.calendar.component.CalendarPager
import com.yujin.record.calendar.component.ReviewItemView
import com.yujin.record.calendar.component.WeekDayRow
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(minContentHeight * 1.5f),
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
                            ReviewItemView(
                                review = review,
                                onClick = { onAction(CalendarUiAction.ReviewClicked(review.id)) })
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
