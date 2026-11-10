package com.yujin.record.presentation.reviewform

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yujin.record.domain.model.ImageItem
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.domain.model.Video
import com.yujin.record.domain.repository.ReviewRepository
import com.yujin.record.domain.usecase.GetMovieDetailUseCase
import com.yujin.record.domain.usecase.GetTvSeriesDetailUseCase
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
    private val reviewRepository: ReviewRepository,
    private val searchVideoUseCase: SearchVideoUseCase,
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getTvSeriesDetailUseCase: GetTvSeriesDetailUseCase,
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
        reviewRepository.getReviewById(reviewId)
            .onSuccess { review ->
                _uiState.update { review.toUiState() }
            }.onFailure {
                _uiState.update {
                    it.copy(userMessage = "리뷰를 불러오는데 실패했습니다.")
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
            ReviewFormUiAction.ShownSnackbarMessage -> removeSnackbarMessage()
        }
    }

    private fun closeBottomSheet() {
        _uiState.update { it.copy(overlay = Overlay.None) }
    }

    private fun saveReview() {
        if (_uiState.value.title.isEmpty()) {
            _uiState.update { it.copy(userMessage = "제목을 입력해 주세요.") }
        } else {
            _uiState.update { it.copy(overlay = Overlay.Progress) }

            viewModelScope.launch {
                val review = _uiState.value.toReview()
                val result = if (review.id == 0L) {
                    reviewRepository.insertReview(review)
                } else {
                    reviewRepository.updateReview(review).map { review.id }
                }

                result
                    .onSuccess { id ->
                        _uiEvent.emit(ReviewFormUiEvent.NavigateToDetail(id))
                    }
                    .onFailure {
                        _uiState.update {
                            it.copy(
                                overlay = Overlay.None,
                                userMessage = "저장에 실패했습니다."
                            )
                        }
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

            is SearchVideoResult.EmptyQuery -> _uiState.update {
                it.copy(
                    overlay = Overlay.None,
                    userMessage = "제목을 입력해 주세요."
                )
            }

            is SearchVideoResult.EmptyResult -> _uiState.update {
                it.copy(
                    overlay = Overlay.None,
                    userMessage = "검색 결과가 없습니다."
                )
            }

            is SearchVideoResult.Error -> _uiState.update {
                it.copy(
                    overlay = Overlay.None,
                    userMessage = "네트워크가 불안정 합니다."
                )
            }
        }
    }

    private fun loadVideoDetail(type: ReviewType, mediaId: Int) = viewModelScope.launch {
        _uiState.update { it.copy(overlay = Overlay.Progress) }
        val result = when (type) {
            ReviewType.MOVIE -> getMovieDetailUseCase(mediaId)
            ReviewType.TV_SERIES -> getTvSeriesDetailUseCase(mediaId)
            ReviewType.BOOK -> return@launch
        }
        when (result) {
            is VideoDetailResult.Success -> {
                val video = result.video
                _uiState.update {
                    it.copy(
                        overlay = Overlay.None,
                        title = video.title,
                        type = video.type,
                        creatorName = video.creator,
                        images = listOf(ImageItem(uri = video.posterPath.toUri())),
                    )
                }
            }
            VideoDetailResult.Error -> {
                _uiState.update {
                    it.copy(overlay = Overlay.None, userMessage = "네트워크가 불안정 합니다.")
                }
            }
        }
    }

    private fun searchBooks() {
        // todo kakao api
    }

    private fun removeSnackbarMessage() {
        _uiState.update {
            it.copy(userMessage = null)
        }
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
            it.copy(images = it.images + images.map { uri -> ImageItem(uri = uri) })
        }
    }

    private fun onImageRemoved(index: Int) {
        _uiState.update {
            it.copy(images = it.images.toMutableList().apply {
                removeAt(index)
            })
        }
    }
}
