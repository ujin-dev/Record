package com.yujin.record.feature.calendar

import kotlinx.datetime.LocalDate

sealed interface CalendarUiAction {
    data object PreviousMonthClicked : CalendarUiAction
    data object NextMonthClicked : CalendarUiAction

    data class DateClicked(val date: LocalDate) : CalendarUiAction

    data object CreateReviewClicked : CalendarUiAction
    data class ReviewClicked(val reviewId: Long) : CalendarUiAction
}