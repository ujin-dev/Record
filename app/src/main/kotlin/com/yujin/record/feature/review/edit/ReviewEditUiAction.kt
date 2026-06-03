package com.yujin.record.feature.review.edit

import com.yujin.record.core.model.ReviewType

sealed interface ReviewEditUiAction {
    data object SaveClicked : ReviewEditUiAction
    data object ImagePickerClicked : ReviewEditUiAction
    data object BookmarkSelectorClicked : ReviewEditUiAction

    data class TitleChanged(val title: String) : ReviewEditUiAction
    data class TypeChanged(val type: ReviewType) : ReviewEditUiAction
    data class CreatorNameChanged(val creatorName: String) : ReviewEditUiAction
    data class ContentChanged(val content: String) : ReviewEditUiAction
    data class ShortReviewChanged(val shortReview: String) : ReviewEditUiAction
    data class RatingChanged(val rating: Float) : ReviewEditUiAction
    data class TagChanged(val tag: String) : ReviewEditUiAction
    data class TagAdded(val tag: String) : ReviewEditUiAction
    data class TagRemoved(val tag: String) : ReviewEditUiAction
}
