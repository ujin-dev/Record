package com.yujin.record.data.remote.mapper

import com.yujin.record.data.remote.model.TmdbMediaType
import com.yujin.record.domain.model.ReviewType

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