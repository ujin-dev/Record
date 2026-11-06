package com.yujin.record.reviewdetail

sealed interface ReviewDetailUiAction {
    object BackClicked : ReviewDetailUiAction
    object EditClicked : ReviewDetailUiAction
    object DeleteClicked : ReviewDetailUiAction
}