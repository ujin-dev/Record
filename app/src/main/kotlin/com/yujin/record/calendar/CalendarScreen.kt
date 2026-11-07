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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PostAdd
import androidx.compose.material.icons.outlined.Today
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.yujin.record.calendar.component.EmptyReviewRow
import com.yujin.record.calendar.component.ReviewItemView
import com.yujin.record.calendar.component.WeekDayRow
import com.yujin.record.ui.theme.Dimens
import com.yujin.record.ui.toDisplayString

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CalendarScreen(
    uiState: CalendarUiState,
    onAction: (CalendarUiAction) -> Unit,
) {
    val handlerHeight = 48.dp
    val minContentHeight = 72.dp
    val sheetPeekHeight = handlerHeight + minContentHeight

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
            actions = {
                IconButton(
                    onClick = {
                        onAction(CalendarUiAction.GoToTodayClicked)
                    }
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Today,
                        contentDescription = "오늘로 이동",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
        )

        BottomSheetScaffold(
            sheetContent = {
                if (uiState.monthlyReviews.isEmpty()) {
                    EmptyReviewRow(
                        modifier = Modifier
                            .fillMaxWidth()
                            .wrapContentHeight()
                    )
                } else {
                    LazyColumn(Modifier.fillMaxWidth()) {
                        items(
                            items = uiState.monthlyReviews,
                            key = { review -> review.id }
                        ) { review ->
                            ReviewItemView(
                                review = review,
                                onClick = { onAction(CalendarUiAction.ReviewClicked(review.id)) })
                        }
                    }
                }
            },
            modifier = Modifier.weight(1f),
            scaffoldState = rememberBottomSheetScaffoldState(
                bottomSheetState = rememberStandardBottomSheetState(
                    initialValue = SheetValue.PartiallyExpanded
                )
            ),
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
                    goToTodayRequestId = uiState.goToTodayRequestId,
                    modifier = Modifier.fillMaxHeight()
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
                    text = "${uiState.selectedDate.toDisplayString()} 리뷰 작성",
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}
