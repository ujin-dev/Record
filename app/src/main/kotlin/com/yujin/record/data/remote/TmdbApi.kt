package com.yujin.record.data.remote

import com.yujin.record.data.remote.dto.MovieDetailResponse
import com.yujin.record.data.remote.dto.MovieSearchResponse
import com.yujin.record.data.remote.dto.TvSeriesDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {
    @GET("search/multi")
    suspend fun searchVideo(
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