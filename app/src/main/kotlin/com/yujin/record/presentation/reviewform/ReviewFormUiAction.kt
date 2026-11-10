package com.yujin.record.presentation.reviewform

import android.net.Uri
import com.yujin.record.domain.model.ReviewType

sealed interface ReviewFormUiAction {
    object BackClicked : ReviewFormUiAction
    object BottomSheetClosed : ReviewFormUiAction
    data object SaveClicked : ReviewFormUiAction
    data object BookmarkSelectorClicked : ReviewFormUiAction
    data object SearchClicked : ReviewFormUiAction
    data object ShownSnackbarMessage : ReviewFormUiAction

    data class FieldChanged(val field: ReviewFormField, val value: String) : ReviewFormUiAction
    data class TypeChanged(val type: ReviewType) : ReviewFormUiAction
    data class RatingChanged(val rating: Float) : ReviewFormUiAction
    data class TagChanged(val tag: String) : ReviewFormUiAction
    data class TagAdded(val tag: String) : ReviewFormUiAction
    data class TagRemoved(val tag: String) : ReviewFormUiAction
    data class ImageAdded(val images: List<Uri>) : ReviewFormUiAction
    data class ImageRemoved(val index: Int) : ReviewFormUiAction
    data class SearchResultItemClicked(val type: ReviewType, val mediaId: Int) : ReviewFormUiAction
}

sealed interface ReviewFormField {
    data object Title : ReviewFormField
    data object Content : ReviewFormField
    data object CreatorName : ReviewFormField
}
