package com.yujin.record.reviewform

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material.icons.filled.LiveTv
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material.icons.outlined.Book
import androidx.compose.material.icons.outlined.LiveTv
import androidx.compose.material.icons.outlined.Movie
import androidx.compose.material.icons.outlined.Search
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
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.yujin.record.model.ReviewType
import com.yujin.record.ui.BOTTOM_SHEET_PADDING
import com.yujin.record.ui.MEDIA_COVER_ASPECT_RATIO
import com.yujin.record.ui.theme.Dimens
import com.yujin.record.ui.toDisplayString
import kotlinx.coroutines.launch

private const val MAX_IMAGE_COUNT = 3

@OptIn(ExperimentalMaterial3Api::class)
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
                    onValueChange = { onAction(ReviewFormUiAction.TitleChanged(it)) },
                    label = "제목",
                    placeholder = "작품명을 입력하세요",
                    trailingIcon = {
                        if (uiState.id == 0L && uiState.type == ReviewType.MOVIE) {
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
    trailingIcon: @Composable (() -> Unit)? = null,
    imeAction: ImeAction,
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = { Text(label) },
        placeholder = { Text(placeholder) },
        trailingIcon = trailingIcon,
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
                        Color.Black.copy(alpha = 0.6f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "remove icon",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

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

@Composable
fun SearchResultItemView(state: MediaItem, onClick: () -> Unit) {
    Column(modifier = Modifier.width(100.dp)) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(MEDIA_COVER_ASPECT_RATIO)
                .clip(shape = RoundedCornerShape(8.dp))
                .border(
                    width = 1.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(8.dp),
                )
                .clickable(onClick = onClick),
            contentAlignment = Alignment.Center
        ) {
            state.posterPath?.let {
                AsyncImage(
                    model = state.posterPath,
                    contentDescription = state.title,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(MEDIA_COVER_ASPECT_RATIO)
                        .clip(shape = RoundedCornerShape(8.dp))
                )
            } ?: run {
                Icon(
                    imageVector = Icons.Outlined.Movie,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                )
            }
        }
        Spacer(Modifier.height(4.dp))
        Text(
            text = state.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp),
            textAlign = TextAlign.Center,
            maxLines = 3,
            style = MaterialTheme.typography.bodyMedium,
        )
    }
}