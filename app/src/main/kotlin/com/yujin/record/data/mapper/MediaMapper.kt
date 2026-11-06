package com.yujin.record.data.mapper

import com.yujin.record.data.remote.model.MediaDto
import com.yujin.record.model.Media

fun MediaDto.toMedia() = Media(
    id = id,
    displayName = title ?: name ?: "",
    posterPath = posterPath,
    mediaType = mediaType.toReviewType(),
    releaseDate = releaseDate,
)