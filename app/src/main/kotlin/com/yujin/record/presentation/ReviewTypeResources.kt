package com.yujin.record.presentation

import com.yujin.record.R
import com.yujin.record.domain.model.ReviewType

val ReviewType.stringRes: Int
    get() = when (this) {
        ReviewType.MOVIE -> R.string.label_review_type_movie
        ReviewType.TV_SERIES -> R.string.label_review_type_tv_series
        ReviewType.BOOK -> R.string.label_review_type_book
    }
