package com.yujin.record.feature.review.form

import com.yujin.record.core.model.ReviewType

sealed interface ReviewFormUiAction {
    object BackClicked : ReviewFormUiAction
    data object SaveClicked : ReviewFormUiAction
    data object ImagePickerClicked : ReviewFormUiAction
    data object BookmarkSelectorClicked : ReviewFormUiAction

    data class TitleChanged(val title: String) : ReviewFormUiAction
    data class TypeChanged(val type: ReviewType) : ReviewFormUiAction
    data class CreatorNameChanged(val creatorName: String) : ReviewFormUiAction
    data class ContentChanged(val content: String) : ReviewFormUiAction
    data class ShortReviewChanged(val shortReview: String) : ReviewFormUiAction
    data class RatingChanged(val rating: Float) : ReviewFormUiAction
    data class TagChanged(val tag: String) : ReviewFormUiAction
    data class TagAdded(val tag: String) : ReviewFormUiAction
    data class TagRemoved(val tag: String) : ReviewFormUiAction
}
