package com.yujin.record.domain.usecase

sealed interface ReviewResult<out T> {
    data class Success<T>(val data: T) : ReviewResult<T>
    data object Error : ReviewResult<Nothing>
}
