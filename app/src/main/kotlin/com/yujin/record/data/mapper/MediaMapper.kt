package com.yujin.record.data.mapper

import com.yujin.record.data.remote.model.MediaDto
import com.yujin.record.model.TmdbMedia

fun MediaDto.toMedia() = TmdbMedia(
    id = id,
    displayName = title ?: name ?: "",
    posterPath = posterPath,
    mediaType = mediaType.toReviewType(),
    releaseDate = releaseDate,
)