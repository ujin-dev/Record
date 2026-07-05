package com.yujin.record.feature.review.edit

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
class ReviewEditViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val reviewRepository: ReviewRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewEditUiState())
    val uiState: StateFlow<ReviewEditUiState> = _uiState.asStateFlow()

    private val _uiEvent = MutableSharedFlow<ReviewEditUiEvent>()
    val uiEvent = _uiEvent.asSharedFlow()

    private var currentReviewId: Long? = runCatching {
        savedStateHandle.toRoute<RecordRoute.ReviewEdit>().reviewId
    }.getOrNull()

    init {
        currentReviewId?.let(::loadReview)
    }

    private fun loadReview(reviewId: Long) {
        // todo
    }

    fun onAction(action: ReviewEditUiAction) {
        when (action) {
            is ReviewEditUiAction.TitleChanged -> onTitleChanged(action.title)
            is ReviewEditUiAction.TypeChanged -> onReviewTypeChanged(action.type)
            is ReviewEditUiAction.CreatorNameChanged -> onCreatorNameChanged(action.creatorName)
            is ReviewEditUiAction.ContentChanged -> onContentChanged(action.content)
            is ReviewEditUiAction.ShortReviewChanged -> onShortReviewChanged(action.shortReview)

            is ReviewEditUiAction.RatingChanged -> onRatingChanged(action.rating)

            is ReviewEditUiAction.TagChanged -> onTagChanged(action.tag)
            is ReviewEditUiAction.TagAdded -> onTagAdded(action.tag)
            is ReviewEditUiAction.TagRemoved -> onTagRemoved(action.tag)

            ReviewEditUiAction.SaveClicked -> saveReview()
            ReviewEditUiAction.ImagePickerClicked -> openImagePicker()
            ReviewEditUiAction.BookmarkSelectorClicked -> openBookmarkSelector()
        }
    }

    private fun saveReview() {
        viewModelScope.launch {
            reviewRepository.createReview(_uiState.value.toReview())
                .onSuccess { id ->
                    _uiEvent.emit(ReviewEditUiEvent.NavigateToDetail(id))
                }
                .onFailure { e ->
                    _uiEvent.emit(ReviewEditUiEvent.ShowToast("저장에 실패했습니다."))
                }
        }
    }

    private fun deleteReview() {}

    private fun openImagePicker() {}

    private fun openBookmarkSelector() {}

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

    fun onImageAdded(images: List<Uri>) {}

    fun onImageRemoved(imageItem: ImageItem) {}

    fun loadBookmarks() {}

    fun applyBookmark(reviewId: Long) {}

}