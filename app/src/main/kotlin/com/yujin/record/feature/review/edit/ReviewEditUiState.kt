package com.yujin.record.feature.review.edit

import android.net.Uri
import com.yujin.record.model.ReviewType

data class ReviewEditUiState(
    val title: String = "",
    val content: String = "",
    val shortReview: String = "",
    val type: ReviewType = ReviewType.MOVIE,
    val creatorName: String = "",
    val rating: Float = 3f,

    val images: List<ImageItem> = emptyList(),
    val tags: List<String> = emptyList(),
    val tagInput: String = "",

    val selectedBookmarkIds: List<Long> = emptyList(),
)

data class ImageItem(
    val id: Long? = null,
    val uri: Uri,
)