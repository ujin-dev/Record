package com.yujin.record.domain.usecase

import com.yujin.record.domain.repository.VideoRepository
import kotlinx.coroutines.CancellationException
import javax.inject.Inject

class GetTvSeriesDetailUseCase @Inject constructor(
    private val videoRepository: VideoRepository,
) {
    suspend operator fun invoke(id: Int): VideoDetailResult = try {
        VideoDetailResult.Success(videoRepository.getDetailTvSeries(id))
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        VideoDetailResult.Error
    }
}
