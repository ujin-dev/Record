package com.yujin.record.feature.review.form

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.yujin.record.app.navigation.RecordRoute
import com.yujin.record.core.data.repository.ReviewRepository
import com.yujin.record.core.model.ImageItem
import com.yujin.record.core.model.ReviewType
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ReviewFormViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewFormUiState())
    val uiState: StateFlow<ReviewFormUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ReviewFormUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var currentReviewId: Long? =
        savedStateHandle.toRoute<RecordRoute.ReviewForm>().reviewId

    init {
        currentReviewId?.let(::loadReview)
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
            is ReviewFormUiAction.ShortReviewChanged -> onShortReviewChanged(action.shortReview)
            is ReviewFormUiAction.RatingChanged -> onRatingChanged(action.rating)
            is ReviewFormUiAction.ImageAdded -> onImageAdded(action.images)
            is ReviewFormUiAction.ImageRemoved -> onImageRemoved(action.index)
            is ReviewFormUiAction.TagChanged -> onTagChanged(action.tag)
            is ReviewFormUiAction.TagAdded -> onTagAdded(action.tag)
            is ReviewFormUiAction.TagRemoved -> onTagRemoved(action.tag)

            ReviewFormUiAction.SaveClicked -> saveReview()
            ReviewFormUiAction.BookmarkSelectorClicked -> openBookmarkSelector()
            ReviewFormUiAction.BackClicked -> backStack()
        }
    }

    private fun saveReview() {
        viewModelScope.launch {
            reviewRepository.createReview(_uiState.value.toReview())
                .onSuccess { id ->
                    _uiEvent.emit(ReviewFormUiEvent.NavigateToDetail(id))
                }
                .onFailure { e ->
                    _uiEvent.emit(ReviewFormUiEvent.ShowToast("저장에 실패했습니다."))
                }
        }
    }

    private fun deleteReview() {}

    private fun openImagePicker() {

    }

    private fun openBookmarkSelector() {}

    private fun backStack() = viewModelScope.launch {
        _uiEvent.emit(ReviewFormUiEvent.NavigateToBack)
    }

    private fun onReviewTypeChanged(type: ReviewType) {
        _uiState.update { state ->
            state.copy(type = type)
        }
    }

    fun onTitleChanged(title: String) {
        _uiState.update { state ->
            state.copy(title = title.trimStart())
        }
    }

    fun onContentChanged(content: String) {
        _uiState.update { state ->
            state.copy(content = content.trimStart())
        }
    }

    fun onShortReviewChanged(shortReview: String) {
        _uiState.update { state ->
            state.copy(shortReview = shortReview.trimStart())
        }
    }

    fun onTagChanged(tag: String) {
        _uiState.update { state ->
            state.copy(tagInput = tag.trim()) // block empty space
        }
    }

    fun onCreatorNameChanged(creatorName: String) {
        _uiState.update { state ->
            state.copy(creatorName = creatorName.trimStart())
        }
    }

    fun onRatingChanged(rating: Float) {
        _uiState.update { state ->
            state.copy(rating = rating)
        }
    }

    fun onTagAdded(tag: String) {
        if (tag.isNotBlank() && !uiState.value.tags.contains(tag)) {
            _uiState.update { state ->
                state.copy(
                    tags = state.tags + tag,
                    tagInput = "",
                )
            }
        }
    }

    fun onTagRemoved(tag: String) {
        _uiState.update { state ->
            state.copy(tags = state.tags - tag)
        }
    }

    private fun validateInput() {}

    fun onImageAdded(images: List<Uri>) {
        _uiState.update { state ->
            state.copy(images = state.images + images.map { ImageItem(uri = it) })
        }
    }

    fun onImageRemoved(index: Int) {
        _uiState.update { state ->
            state.copy(images = state.images.toMutableList().apply {
                removeAt(index)
            })
        }
    }

    fun onImageRemoved(imageItem: ImageItem) {}

    fun loadBookmarks() {}

    fun applyBookmark(reviewId: Long) {}

}