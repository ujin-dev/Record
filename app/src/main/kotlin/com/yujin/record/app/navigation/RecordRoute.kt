package com.yujin.record.app.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface RecordRoute {
    @Serializable
    data object Calendar : RecordRoute

    @Serializable
    data object ReviewCreate : RecordRoute

    @Serializable
    data class ReviewEdit(
        val reviewId: Long
    ) : RecordRoute

    @Serializable
    data class ReviewDetail(
        val reviewId: Long
    ) : RecordRoute
}