package com.yujin.record.presentation.reviewform.component

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
import androidx.compose.ui.res.stringResource
import com.yujin.record.R
import com.yujin.record.domain.model.Video
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.ui.theme.Dimens
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchResultBottomSheet(
    medias: List<Video>,
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
                .padding(bottom = Dimens.bottomSheetPadding)
        ) {
            Text(
                text = stringResource(R.string.bottomsheet_label_select_movie),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(start = Dimens.bottomSheetPadding)
            )

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier
                    .horizontalScroll(rememberScrollState())
                    .fillMaxWidth()
                    .padding(horizontal = Dimens.bottomSheetPadding),
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

