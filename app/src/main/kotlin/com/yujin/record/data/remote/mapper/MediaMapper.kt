package com.yujin.record.data.remote.mapper

import com.yujin.record.data.remote.dto.MediaDto
import com.yujin.record.domain.model.TmdbMedia

fun MediaDto.toMedia() = TmdbMedia(
    id = id,
    displayName = title ?: name ?: "",
    posterPath = posterPath,
    videoType = mediaType.toReviewType(),
    releaseDate = releaseDate,
)