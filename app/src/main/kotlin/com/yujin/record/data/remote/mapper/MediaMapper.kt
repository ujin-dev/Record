package com.yujin.record.data.remote.mapper

import com.yujin.record.data.remote.dto.MediaDto
import com.yujin.record.data.remote.dto.MovieDetailResponse
import com.yujin.record.data.remote.dto.TvSeriesDetailResponse
import com.yujin.record.domain.model.ReviewType
import com.yujin.record.domain.model.Video

private const val TMDB_IMAGE_BASE_URL = "https://image.tmdb.org/t/p/original"

fun MediaDto.toVideo(): Video? {
    val type = mediaType.toReviewType() ?: return null

    return Video(
        id = id,
        title = title ?: name ?: "",
        posterPath = if (posterPath.isNullOrEmpty()) null else "$TMDB_IMAGE_BASE_URL$posterPath",
        type = type,
    )
}

fun MovieDetailResponse.toVideo() = Video(
    id = id,
    title = title,
    posterPath = if (posterPath.isNullOrEmpty()) null else "$TMDB_IMAGE_BASE_URL$posterPath",
    type = ReviewType.MOVIE,
    creator = credits.crew
        .filter { it.job == "Director" }
        .joinToString(", ") { it.name }
)

fun TvSeriesDetailResponse.toVideo() = Video(
    id = id,
    title = name,
    posterPath = if (posterPath.isNullOrEmpty()) null else "$TMDB_IMAGE_BASE_URL$posterPath",
    type = ReviewType.TV_SERIES,
    creator = createdBy.joinToString(", ") { it.name },
)