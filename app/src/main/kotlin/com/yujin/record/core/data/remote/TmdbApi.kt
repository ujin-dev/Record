package com.yujin.record.core.data.remote

import com.yujin.record.core.data.remote.model.MovieDetailResponse
import com.yujin.record.core.data.remote.model.MovieSearchResponse
import com.yujin.record.core.data.remote.model.TvSeriesDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("search/multi")
    suspend fun searchMedia(
        @Query("query") query: String,
        @Query("include_adult") includeAdult: Boolean = true,
        @Query("language") language: String = "ko-KR"
    ): MovieSearchResponse

    @GET("movie/{movie_id}")
    suspend fun getDetailMovie(
        @Path("movie_id") movieId: Int,
        @Query("append_to_response") appendToResponse: String = "credits",
        @Query("language") language: String = "ko-KR"
    ): MovieDetailResponse

    @GET("tv/{series_id}")
    suspend fun getDetailTVSeries(
        @Path("series_id") seriesId: Int,
        @Query("append_to_response") appendToResponse: String = "credits",
        @Query("language") language: String = "ko-KR"
    ): TvSeriesDetailResponse
}