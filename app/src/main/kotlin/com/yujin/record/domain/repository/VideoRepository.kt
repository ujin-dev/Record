package com.yujin.record.domain.repository

import com.yujin.record.domain.model.Video

interface VideoRepository {
    suspend fun searchVideo(query: String): List<Video>
    suspend fun getDetailMovie(id: Int): Video
    suspend fun getDetailTvSeries(id: Int): Video
}