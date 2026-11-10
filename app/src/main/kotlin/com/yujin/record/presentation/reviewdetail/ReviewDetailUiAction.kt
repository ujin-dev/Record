package com.yujin.record.presentation.reviewdetail

sealed interface ReviewDetailUiAction {
    object BackClicked : ReviewDetailUiAction
    object EditClicked : ReviewDetailUiAction
    object DeleteClicked : ReviewDetailUiAction
}