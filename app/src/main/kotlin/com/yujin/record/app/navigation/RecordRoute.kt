package com.yujin.record.app.navigation

sealed class RecordRoute(val route: String) {
    data object Home : RecordRoute("home")
    data object ReviewDetail : RecordRoute("review_detail/{reviewId}") {
        fun createRoute(reviewId: Long) = "review_detail/$reviewId"
    }
    data object ReviewEdit : RecordRoute("review_edit")
}