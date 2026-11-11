package com.yujin.record.domain.usecase

import com.yujin.record.domain.model.ReviewType
import com.yujin.record.domain.repository.VideoRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GetVideoDetailUseCase @Inject constructor(
    private val videoRepository: VideoRepository,
) {
    suspend operator fun invoke(type: ReviewType, id: Int): VideoDetailResult = try {
        val video = when (type) {
            ReviewType.MOVIE -> videoRepository.getDetailMovie(id)
            ReviewType.TV_SERIES -> videoRepository.getDetailTvSeries(id)
            ReviewType.BOOK -> return VideoDetailResult.Error
        }
        VideoDetailResult.Success(video)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        VideoDetailResult.Error
    }
}
