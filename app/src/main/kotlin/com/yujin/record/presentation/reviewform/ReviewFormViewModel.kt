package com.yujin.record.presentation.reviewform

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yujin.record.R
import com.yujin.record.domain.model.ReviewImage
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.domain.model.Video
import com.yujin.record.domain.usecase.GetReviewByIdUseCase
import com.yujin.record.domain.usecase.GetVideoDetailUseCase
import com.yujin.record.domain.usecase.ReviewResult
import com.yujin.record.domain.usecase.SaveReviewUseCase
import com.yujin.record.domain.usecase.SearchVideoResult
import com.yujin.record.domain.usecase.SearchVideoUseCase
import com.yujin.record.domain.usecase.VideoDetailResult
import com.yujin.record.navigation.RecordRoute
import com.yujin.record.presentation.calendar.toInstant
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import javax.inject.Inject


@HiltViewModel
class ReviewFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getReviewByIdUseCase: GetReviewByIdUseCase,
    private val saveReviewUseCase: SaveReviewUseCase,
    private val searchVideoUseCase: SearchVideoUseCase,
    private val getVideoDetailUseCase: GetVideoDetailUseCase,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewFormUiState())
    val uiState: StateFlow<ReviewFormUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ReviewFormUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    init {
        initFromRoute(savedStateHandle.toRoute<RecordRoute.ReviewForm>())
    }

    private fun initFromRoute(route: RecordRoute.ReviewForm) {
        when {
            route.reviewId != null -> loadReview(route.reviewId)
            route.createdDate != null -> {
                val date = LocalDate.parse(route.createdDate).toInstant()
                _uiState.update {
                    it.copy(createdAt = date, updatedAt = date)
                }
            }
        }
    }

    private fun loadReview(reviewId: Long) = viewModelScope.launch {
        when (val result = getReviewByIdUseCase(reviewId)) {
            is ReviewResult.Success -> _uiState.update { result.data.toUiState() }
            ReviewResult.Error -> {
                showSnackbar(R.string.msg_review_load_failed)
            }
        }
    }

    fun onAction(action: ReviewFormUiAction) {
        when (action) {
            is ReviewFormUiAction.FieldChanged -> onFieldChanged(action.field, action.value)
            is ReviewFormUiAction.TypeChanged -> onReviewTypeChanged(action.type)
            is ReviewFormUiAction.RatingChanged -> onRatingChanged(action.rating)
            is ReviewFormUiAction.ImageAdded -> onImageAdded(action.images)
            is ReviewFormUiAction.ImageRemoved -> onImageRemoved(action.index)
            is ReviewFormUiAction.TagChanged -> onTagChanged(action.tag)
            is ReviewFormUiAction.TagAdded -> onTagAdded(action.tag)
            is ReviewFormUiAction.TagRemoved -> onTagRemoved(action.tag)
            is ReviewFormUiAction.SearchResultItemClicked ->
                when (action.type) {
                    ReviewType.MOVIE, ReviewType.TV_SERIES -> loadVideoDetail(
                        action.type,
                        action.mediaId
                    )

                    else -> {}
                }

            ReviewFormUiAction.SaveClicked -> saveReview()
            ReviewFormUiAction.BackClicked -> backStack()
            ReviewFormUiAction.SearchClicked -> search()
            ReviewFormUiAction.BottomSheetClosed -> closeBottomSheet()
            ReviewFormUiAction.BookmarkSelectorClicked -> Unit // todo
        }
    }

    private fun closeBottomSheet() {
        _uiState.update { it.copy(overlay = Overlay.None) }
    }

    private fun saveReview() = viewModelScope.launch {
        if (_uiState.value.title.isEmpty()) {
            showSnackbar(R.string.msg_title_required)
        } else {
            _uiState.update { it.copy(overlay = Overlay.Progress) }

            val review = _uiState.value.toReview()
            when (val result = saveReviewUseCase(review)) {
                is ReviewResult.Success -> {
                    val reviewId = result.data
                    _uiEvent.emit(ReviewFormUiEvent.NavigateToDetail(reviewId))
                }

                ReviewResult.Error -> {
                    showSnackbar(R.string.msg_review_save_failed)
                }
            }
        }
    }

    private fun search() = viewModelScope.launch {
        when (_uiState.value.type) {
            ReviewType.MOVIE, ReviewType.TV_SERIES -> searchVideo()
            ReviewType.BOOK -> searchBooks()
        }
    }

    private suspend fun searchVideo() {
        _uiState.update { it.copy(overlay = Overlay.Progress) }

        when (val result = searchVideoUseCase(_uiState.value.title)) {
            is SearchVideoResult.Success -> {
                if (result.videos.size == 1) {
                    val video = result.videos.first()
                    loadVideoDetail(video.type, video.id)
                } else {
                    _uiState.update {
                        it.copy(
                            overlay = Overlay.BottomSheet,
                            posters = result.videos.map { video ->
                                Video(
                                    id = video.id,
                                    title = video.title,
                                    type = video.type,
                                    posterPath = video.posterPath,
                                )
                            }
                        )
                    }
                }
            }

            is SearchVideoResult.EmptyQuery -> {
                showSnackbar(R.string.msg_title_required)
            }

            is SearchVideoResult.EmptyResult -> {
                showSnackbar(R.string.msg_search_empty)
            }

            is SearchVideoResult.Error -> {
                showSnackbar(R.string.msg_network_unstable)
            }
        }
    }

    private fun loadVideoDetail(type: ReviewType, mediaId: Int) = viewModelScope.launch {
        _uiState.update { it.copy(overlay = Overlay.Progress) }
        if (type == ReviewType.BOOK) return@launch
        when (val result = getVideoDetailUseCase(type, mediaId)) {
            is VideoDetailResult.Success -> {
                val video = result.video
                _uiState.update {
                    it.copy(
                        overlay = Overlay.None,
                        title = video.title,
                        type = video.type,
                        creatorName = video.creator,
                        images = listOf(ReviewImage(uri = video.posterPath.toUri())),
                    )
                }
            }

            VideoDetailResult.Error -> {
                showSnackbar(R.string.msg_network_unstable)
            }
        }
    }

    private fun searchBooks() {
        // todo kakao api
    }

    private fun backStack() = viewModelScope.launch {
        _uiEvent.emit(ReviewFormUiEvent.NavigateToBack)
    }

    private fun onReviewTypeChanged(type: ReviewType) {
        _uiState.update {
            it.copy(type = type)
        }
    }

    private fun onFieldChanged(field: ReviewFormField, value: String) {
        val startTrimValue = value.trimStart()
        _uiState.update {
            when (field) {
                ReviewFormField.Title -> it.copy(title = startTrimValue)
                ReviewFormField.CreatorName -> it.copy(creatorName = startTrimValue)
                ReviewFormField.Content -> it.copy(content = startTrimValue)
            }
        }
    }

    private fun onTagChanged(tag: String) {
        _uiState.update {
            it.copy(tagInput = tag.trimStart())
        }
    }

    private fun onRatingChanged(rating: Float) {
        _uiState.update {
            it.copy(rating = rating)
        }
    }

    private fun onTagAdded(tagContent: String) {
        val currentTags = uiState.value.tags

        val newTags = tagContent.split(Regex("[,\\s]+"))
            .filter { it.isNotEmpty() && !currentTags.contains(it) }
            .distinct()

        if (newTags.isNotEmpty()) {
            _uiState.update {
                it.copy(
                    tags = it.tags + newTags,
                    tagInput = "",
                )
            }
        }
    }

    private fun onTagRemoved(tag: String) {
        _uiState.update {
            it.copy(tags = it.tags - tag)
        }
    }

    private fun onImageAdded(images: List<Uri>) {
        _uiState.update {
            it.copy(images = it.images + images.map { uri -> ReviewImage(uri = uri) })
        }
    }

    private fun onImageRemoved(index: Int) {
        _uiState.update {
            it.copy(images = it.images.toMutableList().apply {
                removeAt(index)
            })
        }
    }

    private suspend fun showSnackbar(messageId: Int) {
        _uiEvent.emit(ReviewFormUiEvent.ShowSnackbar(messageId))
        _uiState.update {
            it.copy(overlay = Overlay.None)
        }
    }
}
