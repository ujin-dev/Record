package com.yujin.record.core.data.repository

import com.yujin.record.core.data.remote.TmdbApi
import com.yujin.record.core.data.remote.model.MovieDetailResponse
import com.yujin.record.core.data.remote.model.MovieDto
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class MovieRepository @Inject constructor(
    private val api: TmdbApi,
) {
    suspend fun searchMovies(query: String): Result<List<MovieDto>> =
        try {
            Result.success(api.searchMovies(query = query).results)
        } catch(e : CancellationException) {
            // cancel coroutine
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }

    suspend fun getMovieDetails(movieId: Int): Result<MovieDetailResponse> =
        try {
            Result.success(api.detailMovie(movieId = movieId))
        } catch (e: CancellationException) {
            // cancel coroutine
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}