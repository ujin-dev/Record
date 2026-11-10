package com.yujin.record.domain.usecase

import android.util.Log
import com.yujin.record.domain.model.Video
import com.yujin.record.domain.repository.VideoRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class SearchVideoUseCase @Inject constructor(
    private val videoRepository: VideoRepository
) {
    suspend operator fun invoke(query: String): SearchVideoResult {
        val trimQuery = query.trim()

        if (trimQuery.isEmpty()) {
            return SearchVideoResult.EmptyQuery
        }

        try {
            val videos = videoRepository.searchVideo(trimQuery)
            return if (videos.isEmpty()) SearchVideoResult.EmptyResult
            else SearchVideoResult.Success(videos)

        } catch (e: CancellationException) {
            throw e

        } catch (e: Exception) {
            Log.e("SearchVideoUseCase", "Failed to search video", e)
            return SearchVideoResult.Error
        }
    }
}

sealed interface SearchVideoResult {
    data class Success(val videos: List<Video>) : SearchVideoResult
    data object Error : SearchVideoResult
    data object EmptyQuery : SearchVideoResult
    data object EmptyResult : SearchVideoResult
}
