package com.yujin.record.reviewform

import android.net.Uri
import com.yujin.record.model.ReviewType

sealed interface ReviewFormUiAction {
    object BackClicked : ReviewFormUiAction
    object BottomSheetClosed : ReviewFormUiAction
    data object SaveClicked : ReviewFormUiAction
    data object BookmarkSelectorClicked : ReviewFormUiAction
    data object SearchClicked : ReviewFormUiAction
    data object ShownSnackbarMessage : ReviewFormUiAction

    data class TitleChanged(val title: String) : ReviewFormUiAction
    data class TypeChanged(val type: ReviewType) : ReviewFormUiAction
    data class CreatorNameChanged(val creatorName: String) : ReviewFormUiAction
    data class ContentChanged(val content: String) : ReviewFormUiAction
    data class RatingChanged(val rating: Float) : ReviewFormUiAction
    data class TagChanged(val tag: String) : ReviewFormUiAction
    data class TagAdded(val tag: String) : ReviewFormUiAction
    data class TagRemoved(val tag: String) : ReviewFormUiAction
    data class ImageAdded(val images: List<Uri>) : ReviewFormUiAction
    data class ImageRemoved(val index: Int) : ReviewFormUiAction
    data class SearchResultItemClicked(val type: ReviewType, val mediaId: Int) : ReviewFormUiAction
}
