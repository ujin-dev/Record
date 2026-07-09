package com.yujin.record.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface RecordRoute {
    @Serializable
    data object Calendar : RecordRoute

    @Serializable
    data class ReviewForm(
        val reviewId: Long? = null
    ) : RecordRoute

    @Serializable
    data class ReviewDetail(
        val reviewId: Long
    ) : RecordRoute
}