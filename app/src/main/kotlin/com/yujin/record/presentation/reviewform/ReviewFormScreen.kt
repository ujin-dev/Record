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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.yujin.record.R
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.presentation.MAX_IMAGE_COUNT
import com.yujin.record.presentation.reviewform.component.FormField
import com.yujin.record.presentation.reviewform.component.FormSection
import com.yujin.record.presentation.reviewform.component.RatingBar
import com.yujin.record.presentation.reviewform.component.ReviewFormBottomBar
import com.yujin.record.presentation.reviewform.component.ReviewFormTopAppBar
import com.yujin.record.presentation.reviewform.component.SearchResultBottomSheet
import com.yujin.record.presentation.reviewform.component.SelectedImageItem
import com.yujin.record.presentation.stringRes
import com.yujin.record.presentation.toDisplayString
import com.yujin.record.ui.theme.Dimens

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
                title = stringResource(if (uiState.id == 0L) R.string.title_review_create else R.string.title_review_edit),
                description = uiState.createdAt.toDisplayString(),
                backClick = { onAction(ReviewFormUiAction.BackClicked) }
            )
        },
        bottomBar = {
            ReviewFormBottomBar(
                text = stringResource(if (uiState.id == 0L) R.string.btn_review_save else R.string.btn_review_edit_complete),
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
            FormSection(
                title = stringResource(R.string.review_form_title_content_type),
                description = stringResource(R.string.review_form_desc_content_type)
            ) {
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
                                label = { Text(stringResource(type.stringRes)) },
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
                    label = stringResource(R.string.review_form_label_title),
                    placeholder = stringResource(R.string.review_form_placeholder_title),
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
                                    contentDescription = stringResource(R.string.content_desc_search),
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
                    ) stringResource(R.string.review_form_label_creator) else stringResource(R.string.review_form_label_author),
                    placeholder = stringResource(R.string.review_form_placeholder_creator),
                    imeAction = ImeAction.Next,
                )
            }

            FormSection(
                title = stringResource(R.string.review_form_title_impression),
                description = stringResource(R.string.review_form_desc_impression)
            ) {
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
                    placeholder = { Text(stringResource(R.string.review_form_placeholder_content)) },
                    shape = RoundedCornerShape(Dimens.cornerSize),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                    ),
                )
            }

            FormSection(
                title = stringResource(R.string.review_form_title_photos),
                description = stringResource(R.string.review_form_desc_photo_count, MAX_IMAGE_COUNT)
            ) {
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
                        Text(stringResource(R.string.btn_add_photo))
                    }
                }
            }

            FormSection(
                title = stringResource(R.string.review_form_title_tags),
                description = stringResource(R.string.review_form_desc_tags)
            ) {
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
                                            contentDescription = stringResource(
                                                R.string.content_desc_tag_remove,
                                                tag
                                            )
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
                    placeholder = { Text(stringResource(R.string.review_form_placeholder_tag)) },
                    trailingIcon = {
                        IconButton(onClick = { onAction(ReviewFormUiAction.TagAdded(uiState.tagInput)) }) {
                            Icon(
                                Icons.Filled.Add,
                                contentDescription = stringResource(R.string.content_desc_add_tag)
                            )
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

            uiState.userMessage?.let { userMessageRes ->
                val userMessage = stringResource(userMessageRes)
                LaunchedEffect(snackbarHostState, uiState, userMessage) {
                    snackbarHostState.showSnackbar(userMessage)
                    onAction(ReviewFormUiAction.ShownSnackbarMessage)
                }
            }
        }
    }
}
