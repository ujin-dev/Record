package com.yujin.record.feature.review.edit

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.yujin.record.core.model.ReviewType
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ReviewEditViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(ReviewEditUiState())
    val uiState: StateFlow<ReviewEditUiState> = _uiState.asStateFlow()

    private var currentReviewId: Long? = null

    fun initialize(reviewId: Long?) {
        if (reviewId != null && reviewId != currentReviewId) {
            currentReviewId = reviewId
            loadReview(reviewId)
        }
    }

    private fun loadReview(reviewId: Long) {}

    fun onAction(action: ReviewEditAction) {
        when (action) {
            is ReviewEditAction.TitleChanged -> onTitleChanged(action.title)
            is ReviewEditAction.TypeChanged -> onReviewTypeChanged(action.type)
            is ReviewEditAction.CreatorNameChanged -> onCreatorNameChanged(action.creatorName)
            is ReviewEditAction.ContentChanged -> onContentChanged(action.content)
            is ReviewEditAction.ShortReviewChanged -> onShortReviewChanged(action.shortReview)

            is ReviewEditAction.RatingChanged -> onRatingChanged(action.rating)

            is ReviewEditAction.TagChanged -> onTagChanged(action.tag)
            is ReviewEditAction.TagAdded -> onTagAdded(action.tag)
            is ReviewEditAction.TagRemoved -> onTagRemoved(action.tag)

            ReviewEditAction.SaveClicked -> saveReview()
            ReviewEditAction.ImagePickerClicked -> openImagePicker()
            ReviewEditAction.BookmarkSelectorClicked -> openBookmarkSelector()
        }
    }

    private fun saveReview() {}

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