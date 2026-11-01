package com.yujin.record.core.data.mapper

import com.yujin.record.core.data.remote.model.TmdbMediaType
import com.yujin.record.core.model.ReviewType

fun ReviewType.toTmdbMediaType(): TmdbMediaType =
    when (this) {
        ReviewType.MOVIE -> TmdbMediaType.MOVIE
        ReviewType.TV_SERIES -> TmdbMediaType.TV
        else -> throw IllegalArgumentException("Unsupported review type: $this")
    }

fun TmdbMediaType.toReviewType(): ReviewType? =
    when (this) {
        TmdbMediaType.MOVIE -> ReviewType.MOVIE
        TmdbMediaType.TV -> ReviewType.TV_SERIES
        else -> null
    }