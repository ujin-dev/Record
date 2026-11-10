package com.yujin.record.presentation.reviewform

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.InputChip
import androidx.compose.material3.LocalMinimumInteractiveComponentSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.presentation.reviewform.component.FormField
import com.yujin.record.presentation.reviewform.component.FormSection
import com.yujin.record.presentation.reviewform.component.RatingBar
import com.yujin.record.presentation.reviewform.component.ReviewFormBottomBar
import com.yujin.record.presentation.reviewform.component.ReviewFormTopAppBar
import com.yujin.record.presentation.reviewform.component.SearchResultBottomSheet
import com.yujin.record.presentation.reviewform.component.SelectedImageItem
import com.yujin.record.ui.theme.Dimens
import com.yujin.record.presentation.toDisplayString

private const val MAX_IMAGE_COUNT = 3

@Composable
fun ReviewFormScreen(
    uiState: ReviewFormUiState,
    onAction: (ReviewFormUiAction) -> Unit,
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
) {
    val remainingImageCount = MAX_IMAGE_COUNT - uiState.images.size
    val launcher = if (remainingImageCount > 1) {
        rememberLauncherForActivityResult(
            ActivityResultContracts.PickMultipleVisualMedia(maxItems = remainingImageCount),
        ) { uris ->
            if (uris.isNotEmpty()) onAction(ReviewFormUiAction.ImageAdded(uris))
        }
    } else {
        rememberLauncherForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
            uri?.let { onAction(ReviewFormUiAction.ImageAdded(listOf(it))) }
        }
    }

    // Bring the content field into view when its content changes.
    val contentIntoViewRequester = remember { BringIntoViewRequester() }
    var isContentFieldFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isContentFieldFocused, uiState.content) {
        if (isContentFieldFocused) contentIntoViewRequester.bringIntoView()
    }

    // Bring the tag field into view when its tag changes.
    val tagIntoViewRequester = remember { BringIntoViewRequester() }
    var isTagFieldFocused by remember { mutableStateOf(false) }

    LaunchedEffect(isTagFieldFocused, uiState.tags) {
        if (isTagFieldFocused) tagIntoViewRequester.bringIntoView()
    }

    val keyboardController = LocalSoftwareKeyboardController.current
    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            ReviewFormTopAppBar(
                title = if (uiState.id == 0L) "리뷰 작성" else "리뷰 수정",
                description = uiState.createdAt.toDisplayString(),
                backClick = { onAction(ReviewFormUiAction.BackClicked) }
            )
        },
        bottomBar = {
            ReviewFormBottomBar(
                text = if (uiState.id == 0L) "리뷰 저장" else "수정 완료",
                onClick = { onAction(ReviewFormUiAction.SaveClicked) }
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .pointerInput(Unit) {
                    detectTapGestures {
                        keyboardController?.hide()
                        focusManager.clearFocus()
                    }
                }
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(
                    start = Dimens.screenPadding,
                    end = Dimens.screenPadding,
                    bottom = Dimens.screenPadding
                ),
            verticalArrangement = Arrangement.spacedBy(Dimens.cardBetweenPadding),
        ) {
            FormSection(title = "무엇을 기록할까요?", description = "콘텐츠 종류를 선택하세요") {
                Row(
                    modifier = Modifier.wrapContentHeight(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    ReviewType.entries.forEach { type ->
                        val selected = uiState.type == type
                        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                            FilterChip(
                                selected = selected,
                                onClick = { onAction(ReviewFormUiAction.TypeChanged(type)) },
                                label = { Text(type.displayName) },
                                modifier = Modifier.wrapContentHeight(),
                                leadingIcon = {
                                    Icon(
                                        imageVector = when (type) {
                                            ReviewType.MOVIE -> if (selected) Icons.Filled.Movie else Icons.Outlined.Movie
                                            ReviewType.TV_SERIES -> if (selected) Icons.Filled.LiveTv else Icons.Outlined.LiveTv
                                            ReviewType.BOOK -> if (selected) Icons.Filled.Book else Icons.Outlined.Book
                                        },
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp),
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
                                    labelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    iconColor = MaterialTheme.colorScheme.onSurfaceVariant,
                                    selectedContainerColor = MaterialTheme.colorScheme.primary,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimary
                                ),
                                border = null,
                            )
                        }
                    }
                }
                FormField(
                    value = uiState.title,
                    onValueChange = {
                        onAction(
                            ReviewFormUiAction.FieldChanged(
                                ReviewFormField.Title,
                                it
                            )
                        )
                    },
                    label = "제목",
                    placeholder = "작품명을 입력하세요",
                    trailingIcon = {
                        if (uiState.id == 0L && uiState.type in listOf(
                                ReviewType.MOVIE,
                                ReviewType.TV_SERIES
                            )
                        ) {
                            IconButton(onClick = {
                                keyboardController?.hide()
                                focusManager.clearFocus()

                                onAction(ReviewFormUiAction.SearchClicked)
                            }) {
                                Icon(
                                    imageVector = Icons.Outlined.Search,
                                    contentDescription = "search",
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    },
                    imeAction = ImeAction.Next,
                )
                FormField(
                    value = uiState.creatorName,
                    onValueChange = {
                        onAction(
                            ReviewFormUiAction.FieldChanged(
                                ReviewFormField.CreatorName,
                                it
                            )
                        )
                    },
                    label = if (uiState.type in listOf(
                            ReviewType.MOVIE,
                            ReviewType.TV_SERIES
                        )
                    ) "제작자" else "저자",
                    placeholder = "이름을 입력하세요",
                    imeAction = ImeAction.Next,
                )
            }

            FormSection(title = "감상", description = "별점과 기억하고 싶은 장면을 남겨보세요") {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    RatingBar(
                        rating = uiState.rating,
                        onRatingChange = {
                            onAction(ReviewFormUiAction.RatingChanged(it))
                        },
                    )
                    Text(
                        "%.1f".format(uiState.rating),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = {
                        onAction(
                            ReviewFormUiAction.FieldChanged(
                                ReviewFormField.Content,
                                it
                            )
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 48.dp)
                        .bringIntoViewRequester(contentIntoViewRequester)
                        .onFocusChanged { isContentFieldFocused = it.isFocused },
                    placeholder = { Text("어떤 생각이 들었나요?") },
                    shape = RoundedCornerShape(Dimens.cornerSize),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
            }

            FormSection(title = "사진", description = "최대 ${MAX_IMAGE_COUNT}장까지 추가할 수 있어요") {
                if (uiState.images.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(Dimens.cardBetweenPadding)
                    ) {
                        repeat(MAX_IMAGE_COUNT) { index ->
                            uiState.images.getOrNull(index)?.uri?.let { imageUri ->
                                SelectedImageItem(
                                    imageUri = imageUri,
                                    onRemove = { onAction(ReviewFormUiAction.ImageRemoved(index)) },
                                    modifier = Modifier.weight(1f)
                                )
                            } ?: Spacer(Modifier.weight(1f))
                        }
                    }
                }
                if (remainingImageCount > 0) {
                    OutlinedButton(
                        onClick = {
                            keyboardController?.hide()
                            focusManager.clearFocus()
                            launcher.launch(
                                PickVisualMediaRequest(
                                    ActivityResultContracts.PickVisualMedia.ImageOnly,
                                )
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(Dimens.cornerSize),
                    ) {
                        Icon(Icons.Filled.AddPhotoAlternate, contentDescription = null)
                        Spacer(Modifier.size(8.dp))
                        Text("사진 추가")
                    }
                }
            }

            FormSection(title = "태그", description = "나중에 쉽게 찾아볼 수 있도록 분류해요") {
                if (uiState.tags.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        uiState.tags.forEach { tag ->
                            CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
                                InputChip(
                                    selected = true,
                                    onClick = { onAction(ReviewFormUiAction.TagRemoved(tag)) },
                                    label = { Text(tag) },
                                    trailingIcon = {
                                        Icon(
                                            Icons.Filled.Close,
                                            contentDescription = "$tag 삭제"
                                        )
                                    },
                                )
                            }
                        }
                    }
                }
                OutlinedTextField(
                    value = uiState.tagInput,
                    onValueChange = { onAction(ReviewFormUiAction.TagChanged(it)) },
                    modifier = Modifier
                        .bringIntoViewRequester(tagIntoViewRequester)
                        .onFocusChanged { isTagFieldFocused = it.isFocused }
                        .fillMaxWidth(),
                    placeholder = { Text("예: SF, 로맨스") },
                    trailingIcon = {
                        IconButton(onClick = { onAction(ReviewFormUiAction.TagAdded(uiState.tagInput)) }) {
                            Icon(Icons.Filled.Add, contentDescription = "태그 추가")
                        }
                    },
                    singleLine = true,
                    shape = RoundedCornerShape(Dimens.cornerSize),
                )
            }

            if (uiState.overlay == Overlay.BottomSheet) {
                SearchResultBottomSheet(
                    medias = uiState.posters,
                    onClick = { type, mediaId ->
                        onAction(
                            ReviewFormUiAction.SearchResultItemClicked(
                                type,
                                mediaId
                            )
                        )
                    },
                    onDismiss = { onAction(ReviewFormUiAction.BottomSheetClosed) }
                )
            }

            uiState.userMessage?.let { userMessage ->
                LaunchedEffect(snackbarHostState, uiState, userMessage) {
                    snackbarHostState.showSnackbar(userMessage)
                    onAction(ReviewFormUiAction.ShownSnackbarMessage)
                }
            }
        }
    }
}
