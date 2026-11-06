package com.yujin.record.reviewform.component

import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yujin.record.model.ReviewType
import com.yujin.record.model.MediaItem
import com.yujin.record.ui.BOTTOM_SHEET_PADDING
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultBottomSheet(
    medias: List<MediaItem>,
    onClick: (ReviewType, Int) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()

    ModalBottomSheet(
        sheetState = sheetState,
        onDismissRequest = onDismiss
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(bottom = BOTTOM_SHEET_PADDING.dp)
        ) {
            Text(
                text = "영화를 선택해 주세요.",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = BOTTOM_SHEET_PADDING.dp)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(horizontal = BOTTOM_SHEET_PADDING.dp),
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                medias.forEach { media ->
                    SearchResultItemView(
                        state = media,
                    ) {
                        scope.launch { sheetState.hide() }
                        onClick(media.type, media.id)
                    }
                }
            }
        }
    }
}

