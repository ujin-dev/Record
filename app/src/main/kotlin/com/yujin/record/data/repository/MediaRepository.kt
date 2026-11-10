package com.yujin.record.data.repository

import com.yujin.record.data.remote.mapper.toMedia
import com.yujin.record.data.remote.TmdbApi
import com.yujin.record.data.remote.dto.MovieDetailResponse
import com.yujin.record.data.remote.dto.TvSeriesDetailResponse
import com.yujin.record.domain.model.TmdbMedia
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val api: TmdbApi,
) {
    suspend fun searchVideo(query: String): Result<List<TmdbMedia>> =
        try {
            Result.success(
                api.searchVideo(query = query)
                    .results
                    .mapNotNull { dto ->
                        dto.toMedia().takeIf { it.videoType != null }
                    }
            )
        } catch (e: CancellationException) {
            // cancel coroutine
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getDetailMovie(movieId: Int): Result<MovieDetailResponse> =
        try {
            Result.success(api.getDetailMovie(movieId = movieId))
        } catch (e: CancellationException) {
            // cancel coroutine
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getDetailTvSeries(seriesId: Int): Result<TvSeriesDetailResponse> =
        try {
            Result.success(api.getDetailTVSeries(seriesId = seriesId))
        } catch (e: CancellationException) {
            // cancel coroutine
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}