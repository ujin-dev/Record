package com.yujin.record.di

import android.content.Context
import androidx.room.Room
import com.yujin.record.BuildConfig
import com.yujin.record.database.AppDatabase
import com.yujin.record.database.dao.ImageDao
import com.yujin.record.database.dao.ReviewDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

const val DATABASE_NAME = "record.db"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            DATABASE_NAME
        ).apply {
            if (BuildConfig.DEBUG) {
                fallbackToDestructiveMigration(true)
            }
        }.build()

    @Provides
    fun provideReviewDao(databases: AppDatabase): ReviewDao = databases.reviewDao()

    @Provides
    fun provideImageDao(databases: AppDatabase): ImageDao = databases.imageDao()
}
