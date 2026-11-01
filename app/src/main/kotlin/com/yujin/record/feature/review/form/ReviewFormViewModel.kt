package com.yujin.record.feature.review.form

import android.net.Uri
import android.util.Log
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yujin.record.app.navigation.RecordRoute
import com.yujin.record.core.data.repository.MediaRepository
import com.yujin.record.core.data.repository.ReviewRepository
import com.yujin.record.core.model.ImageItem
import com.yujin.record.core.model.Media
import com.yujin.record.core.model.ReviewType
import com.yujin.record.feature.calendar.toInstant
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

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/h632"

@HiltViewModel
class ReviewFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
    private val mediaRepository: MediaRepository,
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
                _uiState.update { currentState ->
                    currentState.copy(createdAt = date, updatedAt = date)
                }
            }
        }
    }

    private fun loadReview(reviewId: Long) = viewModelScope.launch {
        reviewRepository.getReviewById(reviewId)
            .onSuccess { review ->
                _uiState.update { review.toUiState() }
            }.onFailure {
                _uiEvent.emit(ReviewFormUiEvent.ShowToast("리뷰를 불러오는데 실패했습니다."))
            }
    }

    fun onAction(action: ReviewFormUiAction) {
        when (action) {
            is ReviewFormUiAction.TitleChanged -> onTitleChanged(action.title)
            is ReviewFormUiAction.TypeChanged -> onReviewTypeChanged(action.type)
            is ReviewFormUiAction.CreatorNameChanged -> onCreatorNameChanged(action.creatorName)
            is ReviewFormUiAction.ContentChanged -> onContentChanged(action.content)
            is ReviewFormUiAction.RatingChanged -> onRatingChanged(action.rating)
            is ReviewFormUiAction.ImageAdded -> onImageAdded(action.images)
            is ReviewFormUiAction.ImageRemoved -> onImageRemoved(action.index)
            is ReviewFormUiAction.TagChanged -> onTagChanged(action.tag)
            is ReviewFormUiAction.TagAdded -> onTagAdded(action.tag)
            is ReviewFormUiAction.TagRemoved -> onTagRemoved(action.tag)
            is ReviewFormUiAction.SearchResultItemClicked ->
                when (action.type) {
                    ReviewType.MOVIE -> getMovieDetail(action.mediaId)
                    ReviewType.TV_SERIES -> getTvSeriesDetail(action.mediaId)
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
        _uiState.update { state -> state.copy(overlay = Overlay.None) }
    }

    private fun saveReview() {
        _uiState.update { state -> state.copy(overlay = Overlay.Progress) }

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
                    _uiState.update { state -> state.copy(overlay = Overlay.None) }
                    _uiEvent.emit(ReviewFormUiEvent.ShowToast("저장에 실패했습니다."))
                }
        }
    }

    private fun search() = viewModelScope.launch {
        when (_uiState.value.type) {
            ReviewType.MOVIE, ReviewType.TV_SERIES -> searchMedia()
            ReviewType.BOOK -> searchBooks()
        }
    }

    private suspend fun searchMedia() {
        val title = _uiState.value.title

        if (title.isEmpty()) {
            // todo empty query

        } else {
            _uiState.update { state -> state.copy(overlay = Overlay.Progress) }

            mediaRepository.searchMedia(_uiState.value.title)
                .onSuccess { result: List<Media> ->
                    Log.d("search", result.size.toString())

                    when {
                        result.isEmpty() -> {
                            // todo snack bar empty result
                        }

                        result.size == 1 -> {
                            _uiState.update { state -> state.copy(overlay = Overlay.Progress) }

                            val media = result.first()
                            when (media.mediaType) {
                                ReviewType.MOVIE -> getMovieDetail(media.id)
                                ReviewType.TV_SERIES -> getTvSeriesDetail(media.id)
                                else -> Unit
                            }
                        }

                        else -> {
                            _uiState.update { state ->
                                state.copy(
                                    overlay = Overlay.BottomSheet,
                                    posters = result.mapNotNull { item ->
                                        item.mediaType?.let { reviewType ->
                                            MediaItem(
                                                id = item.id,
                                                title = item.displayName,
                                                type = reviewType,
                                                posterPath = item.posterPath
                                                    ?.takeIf(String::isNotEmpty)
                                                    ?.let { path -> "$TMDB_IMAGE_BASE_URL$path" })
                                        }
                                    }
                                )
                            }
                        }
                    }
                }.onFailure { e ->
                    // todo snack bar network error
                    e.message?.let { msg -> Log.e("search", msg) }
                }
        }
    }

    private fun getMovieDetail(movieId: Int) {
        viewModelScope.launch {
            mediaRepository.getDetailMovie(movieId)
                .onSuccess { result ->
                    _uiState.update { state ->
                        state.copy(
                            overlay = Overlay.None,
                            title = result.title,
                            type = ReviewType.MOVIE,
                            creatorName = result.credits.crew
                                .filter { it.job == "Director" }
                                .joinToString(", ") { it.name },
                            images = result.posterPath
                                ?.let {
                                    listOf(
                                        ImageItem(
                                            uri = "$TMDB_IMAGE_BASE_URL${result.posterPath}".toUri()
                                        )
                                    )
                                }
                                ?: emptyList(),
                            tags = result.genres.map { it.name }
                        )
                    }
                }
                .onFailure {
                    // todo network error
                }
        }
    }

    private fun getTvSeriesDetail(seriesId: Int) {
        viewModelScope.launch {
            mediaRepository.getDetailTvSeries(seriesId)
                .onSuccess { result ->
                    _uiState.update { state ->
                        state.copy(
                            overlay = Overlay.None,
                            title = result.name,
                            type = ReviewType.TV_SERIES,
                            creatorName = result.createdBy.joinToString(", ") { it.name },
                            images = result.posterPath
                                ?.let {
                                    listOf(
                                        ImageItem(
                                            uri = "$TMDB_IMAGE_BASE_URL${result.posterPath}".toUri()
                                        )
                                    )
                                }
                                ?: emptyList(),
                            tags = result.genres.map { it.name }
                        )
                    }
                }
                .onFailure {
                    // todo network error
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
        _uiState.update { state ->
            state.copy(type = type)
        }
    }

    private fun onTitleChanged(title: String) {
        _uiState.update { state ->
            state.copy(title = title.trimStart())
        }
    }

    private fun onContentChanged(content: String) {
        _uiState.update { state ->
            state.copy(content = content.trimStart())
        }
    }

    private fun onTagChanged(tag: String) {
        _uiState.update { state -> state.copy(tagInput = tag.trimStart()) }
    }

    private fun onCreatorNameChanged(creatorName: String) {
        _uiState.update { state ->
            state.copy(creatorName = creatorName.trimStart())
        }
    }

    private fun onRatingChanged(rating: Float) {
        _uiState.update { state ->
            state.copy(rating = rating)
        }
    }

    private fun onTagAdded(tagContent: String) {
        val currentTags = uiState.value.tags

        val newTags = tagContent.split(Regex("[,\\s]+"))
            .filter { it.isNotEmpty() && !currentTags.contains(it) }
            .distinct()

        if (newTags.isNotEmpty()) {
            _uiState.update { state ->
                state.copy(
                    tags = state.tags + newTags,
                    tagInput = "",
                )
            }
        }
    }

    private fun onTagRemoved(tag: String) {
        _uiState.update { state ->
            state.copy(tags = state.tags - tag)
        }
    }

    private fun onImageAdded(images: List<Uri>) {
        _uiState.update { state ->
            state.copy(images = state.images + images.map { ImageItem(uri = it) })
        }
    }

    private fun onImageRemoved(index: Int) {
        _uiState.update { state ->
            state.copy(images = state.images.toMutableList().apply {
                removeAt(index)
            })
        }
    }
}
