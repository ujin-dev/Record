package com.yujin.record.core.data.mapper

import com.yujin.record.core.data.remote.model.MediaDto
import com.yujin.record.core.model.Media

fun MediaDto.toMedia() = Media(
    id = id,
    displayName = title ?: name ?: "",
    posterPath = posterPath,
    mediaType = mediaType.toReviewType(),
    releaseDate = releaseDate,
)