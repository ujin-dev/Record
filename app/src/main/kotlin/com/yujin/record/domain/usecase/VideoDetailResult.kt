package com.yujin.record.domain.usecase

import com.yujin.record.domain.model.Video

sealed interface VideoDetailResult {
    data class Success(val video: Video) : VideoDetailResult
    data object Error : VideoDetailResult
}
