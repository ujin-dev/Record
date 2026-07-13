package com.yujin.record.feature.review.form

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircleOutline
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.InputChipDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.yujin.record.core.model.ReviewType
import com.yujin.record.core.ui.preview.DefaultScreenPreviews
import com.yujin.record.core.ui.theme.RecordTheme
import com.yujin.record.feature.review.component.ImageList

private val REVIEW_TYPE_OPTIONS = ReviewType.entries
private const val MAX_IMAGE_COUNT = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewFormScreen(
    uiState: ReviewFormUiState,
    onAction: (ReviewFormUiAction) -> Unit,
) {
    val remainImageSize = MAX_IMAGE_COUNT - uiState.images.size

    // PickMultipleVisualMedia requires maxItems >=2.
    // otherwise, it throws on exception.
    val launcher = if (remainImageSize > 1) {
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(
                maxItems = remainImageSize
            )
        ) { uris ->
            if (uris.isNotEmpty()) {
                onAction(ReviewFormUiAction.ImageAdded(uris))
            }
        }
    } else {
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickVisualMedia()
        ) { uri ->
            uri?.let {
                onAction(ReviewFormUiAction.ImageAdded(listOf(it)))
            }
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(text = "리뷰 작성") },
                navigationIcon = {
                    IconButton(onClick = { onAction(ReviewFormUiAction.BackClicked) }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Rounded.ArrowBack,
                            contentDescription = "뒤로가기"
                        )
                    }
                },
                actions = {
                    IconButton(
                        onClick = {
                            launcher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly
                                )
                            )
                        },
                        enabled = remainImageSize > 0,
                    ) {
                        Icon(
                            imageVector = Icons.Default.AddPhotoAlternate,
                            contentDescription = "사진 가져오기"
                        )
                    }

                    IconButton(
                        onClick = { onAction(ReviewFormUiAction.BookmarkSelectorClicked) },
                        enabled = true,
                    ) {
                        Icon(
                            imageVector = Icons.Default.BookmarkBorder,
                            contentDescription = "북마크"
                        )
                    }

                    IconButton(
                        onClick = { onAction(ReviewFormUiAction.SaveClicked) },
                        enabled = true,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "저장"
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding) // system insets
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 16.dp) // content padding
        ) {
            ImageList(
                images = uiState.images.map { it.uri },
                onRemoveClick = { index -> onAction(ReviewFormUiAction.ImageRemoved(index)) },
            )

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth()
            ) {
                REVIEW_TYPE_OPTIONS.forEachIndexed { index, reviewType ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = REVIEW_TYPE_OPTIONS.size
                        ),
                        onClick = { onAction(ReviewFormUiAction.TypeChanged(reviewType)) },
                        selected = reviewType == uiState.type,
                        label = { Text(text = reviewType.displayName) }
                    )
                }
            }

            // title
            OutlinedTextField(
                value = uiState.title,
                onValueChange = { onAction(ReviewFormUiAction.TitleChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = { Text(text = "제목") },
                singleLine = true,
            )

            // creator
            OutlinedTextField(
                value = uiState.creatorName,
                onValueChange = { onAction(ReviewFormUiAction.CreatorNameChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text(if (uiState.type == ReviewType.MOVIE) "감독" else "저자")
                },
                singleLine = true,
            )

            // content
            OutlinedTextField(
                value = uiState.content,
                onValueChange = { onAction(ReviewFormUiAction.ContentChanged(it)) },
                label = { Text("상세 리뷰") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            // short review
            OutlinedTextField(
                value = uiState.shortReview,
                onValueChange = { onAction(ReviewFormUiAction.ShortReviewChanged(it)) },
                modifier = Modifier.fillMaxWidth(),
                label = {
                    Text("한줄평")
                },
                singleLine = true,
            )

            // rating
            // improvement : https://developer.android.com/develop/ui/compose/touch-input/scroll/scroll-modifiers#scrollable-modifier
            Row {
                repeat(5) { index ->
                    IconButton(onClick = { onAction(ReviewFormUiAction.RatingChanged((index + 1).toFloat())) }) {
                        Icon(
                            imageVector = if (index < uiState.rating) Icons.Default.Star else Icons.Default.StarBorder,
                            tint = MaterialTheme.colorScheme.primary,
                            contentDescription = null
                        )
                    }
                }
            }

            // tag chips & input field
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                uiState.tags.forEach { tag ->
                    InputChip(
                        selected = true,
                        onClick = {},
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "삭제",
                                modifier = Modifier
                                    .size(InputChipDefaults.IconSize)
                                    .clickable {
                                        onAction(ReviewFormUiAction.TagRemoved(tag))
                                    }
                            )
                        },
                        label = { Text(text = tag) },
                        modifier = Modifier.height(InputChipDefaults.Height)
                    )
                }

                OutlinedTextField(
                    value = uiState.tagInput,
                    onValueChange = { onAction(ReviewFormUiAction.TagChanged(it)) },
                    placeholder = { Text("태그 입력") },
                    singleLine = true,
                    trailingIcon = {
                        IconButton(onClick = {
                            onAction(ReviewFormUiAction.TagAdded(uiState.tagInput))
                        }) {
                            Icon(
                                imageVector = Icons.Default.AddCircleOutline,
                                contentDescription = "추가"
                            )
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}
