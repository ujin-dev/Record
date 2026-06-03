package com.yujin.record.feature.review.edit

import com.yujin.record.core.model.ReviewType

sealed interface ReviewEditAction {
    data object SaveClicked : ReviewEditAction
    data object ImagePickerClicked : ReviewEditAction
    data object BookmarkSelectorClicked : ReviewEditAction

    data class TitleChanged(val title: String) : ReviewEditAction
    data class TypeChanged(val type: ReviewType) : ReviewEditAction
    data class CreatorNameChanged(val creatorName: String) : ReviewEditAction
    data class ContentChanged(val content: String) : ReviewEditAction
    data class ShortReviewChanged(val shortReview: String) : ReviewEditAction

    data class RatingChanged(val rating: Float) : ReviewEditAction

    data class TagChanged(val tag: String) : ReviewEditAction
    data class TagAdded(val tag: String) : ReviewEditAction
    data class TagRemoved(val tag: String) : ReviewEditAction
}
