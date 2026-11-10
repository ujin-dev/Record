package com.yujin.record.di

import com.yujin.record.data.repository.ReviewRepositoryImpl
import com.yujin.record.data.repository.VideoRepositoryImpl
import com.yujin.record.domain.repository.ReviewRepository
import com.yujin.record.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    internal abstract fun bindReviewRepository(
        reviewRepositoryImpl: ReviewRepositoryImpl
    ): ReviewRepository

    @Binds
    @Singleton
    internal abstract fun bindVideoRepository(
        videoRepositoryImpl: VideoRepositoryImpl
    ): VideoRepository
}
