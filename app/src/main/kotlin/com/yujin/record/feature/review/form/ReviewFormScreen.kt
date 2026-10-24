package com.yujin.record.feature.review.form

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.StarHalf
import androidx.compose.material.icons.automirrored.rounded.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yujin.record.core.model.ReviewType
import com.yujin.record.core.ui.theme.Dimens
import com.yujin.record.core.ui.toDisplayString

private const val MAX_IMAGE_COUNT = 3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReviewFormScreen(
    uiState: ReviewFormUiState,
    onAction: (ReviewFormUiAction) -> Unit,
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
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = if (uiState.id == 0L) "리뷰 작성" else "리뷰 수정",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.SemiBold,
                        )
                        Text(
                            text = uiState.createdAt.toDisplayString(),
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { onAction(ReviewFormUiAction.BackClicked) }) {
                        Icon(Icons.AutoMirrored.Rounded.ArrowBack, contentDescription = "뒤로가기")
                    }
                },
            )
        },
        bottomBar = {
            Surface(
                shadowElevation = 3.dp,
                color = MaterialTheme.colorScheme.surface
            ) {
                Button(
                    onClick = { onAction(ReviewFormUiAction.SaveClicked) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .windowInsetsPadding(WindowInsets.navigationBars)
                        .padding(Dimens.screenPadding)
                        .imePadding()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    Text(
                        if (uiState.id == 0L) "리뷰 저장" else "수정 완료",
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        },
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
                                            else -> if (selected) Icons.Filled.Book else Icons.Outlined.Book
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
                    onValueChange = { onAction(ReviewFormUiAction.TitleChanged(it)) },
                    label = "제목",
                    placeholder = "작품명을 입력하세요",
                    imeAction = ImeAction.Next,
                )
                FormField(
                    value = uiState.creatorName,
                    onValueChange = { onAction(ReviewFormUiAction.CreatorNameChanged(it)) },
                    label = if (uiState.type == ReviewType.MOVIE) "제작자" else "저자",
                    placeholder = "이름을 입력하세요",
                    imeAction = ImeAction.Next,
                )
            }

            FormSection(title = "감상", description = "별점과 기억하고 싶은 장면을 남겨보세요") {
                var rate by remember(uiState.rating) { mutableFloatStateOf(uiState.rating) }
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .pointerInput(Unit) {
                                detectTapGestures { point ->
                                    val starWidth = size.width / 5f
                                    val index = (point.x / starWidth).toInt().coerceIn(0, 4)
                                    val fraction = (point.x - index * starWidth) / starWidth
                                    rate = index + if (fraction < 0.5f) 0.5f else 1f
                                    onAction(ReviewFormUiAction.RatingChanged(rate))
                                }
                            }
                            .pointerInput(Unit) {
                                detectHorizontalDragGestures { change: PointerInputChange, _ ->
                                    val starWidth = size.width / 5f
                                    val index =
                                        (change.position.x / starWidth).toInt().coerceIn(0, 4)
                                    val fraction =
                                        (change.position.x - index * starWidth) / starWidth
                                    rate = index + if (fraction < 0.5f) 0.5f else 1f
                                    onAction(ReviewFormUiAction.RatingChanged(rate))
                                    change.consume()
                                }
                            },
                    ) {
                        repeat(5) { index ->
                            Icon(
                                imageVector = when {
                                    index + 1 <= rate -> Icons.Filled.Star
                                    index < rate -> Icons.AutoMirrored.Filled.StarHalf
                                    else -> Icons.Filled.StarBorder
                                },
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    }
                    Text(
                        "%.1f".format(rate),
                        style = MaterialTheme.typography.titleLarge,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
                OutlinedTextField(
                    value = uiState.content,
                    onValueChange = { onAction(ReviewFormUiAction.ContentChanged(it)) },
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
        }
    }
}

@Composable
private fun FormSection(
    title: String,
    description: String,
    content: @Composable () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(Dimens.cardCornerSize),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer),
    ) {
        Column(
            modifier = Modifier.padding(Dimens.screenPadding),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    description, style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            content()
        }
    }
}

@Composable
private fun FormField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    placeholder: String,
    imeAction: ImeAction,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        singleLine = true,
        shape = RoundedCornerShape(Dimens.cornerSize),
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            capitalization = KeyboardCapitalization.Sentences,
            imeAction = imeAction,
        ),
    )
}

@Composable
fun SelectedImageItem(imageUri: Uri, onRemove: () -> Unit, modifier: Modifier = Modifier) {
    Box(modifier = modifier) {
        AsyncImage(
            model = imageUri,
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(Dimens.cornerSize)),
            contentScale = ContentScale.Crop,
        )

        CompositionLocalProvider(LocalMinimumInteractiveComponentSize provides 0.dp) {
            IconButton(
                onClick = onRemove,
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(6.dp)
                    .size(32.dp)
                    .background(
                        Color.White.copy(alpha = 0.6f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove icon",
                    tint = Color.DarkGray,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}