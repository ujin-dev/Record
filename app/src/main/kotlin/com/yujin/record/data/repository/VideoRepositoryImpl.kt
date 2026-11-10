package com.yujin.record.data.repository

import com.yujin.record.data.remote.TmdbApi
import com.yujin.record.data.remote.mapper.toVideo
import com.yujin.record.domain.model.Video
import com.yujin.record.domain.repository.VideoRepository
import javax.inject.Inject

class VideoRepositoryImpl @Inject constructor(
    private val api: TmdbApi,
) : VideoRepository {

    override suspend fun searchVideo(query: String): List<Video> =
        api.searchVideo(query = query)
            .results
            .mapNotNull { dto -> dto.toVideo() }

    override suspend fun getDetailMovie(id: Int): Video =
        api.getDetailMovie(movieId = id).toVideo()

    override suspend fun getDetailTvSeries(id: Int): Video =
        api.getDetailTVSeries(seriesId = id).toVideo()
}
