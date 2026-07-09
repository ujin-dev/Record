package com.yujin.record.feature.review.detail

sealed interface ReviewDetailUiAction {
    object BackClicked : ReviewDetailUiAction
    object EditClicked : ReviewDetailUiAction
    object DeleteClicked : ReviewDetailUiAction
}