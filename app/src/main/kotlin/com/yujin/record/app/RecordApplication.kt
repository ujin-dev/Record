package com.yujin.record.app

import android.app.Application
import com.yujin.record.core.data.repository.ReviewRepository
import com.yujin.record.core.data.repository.ReviewRepositoryImpl
import com.yujin.record.core.database.AppDatabase

class RecordApplication : Application() {
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }
    val reviewRepository: ReviewRepository by lazy {
        ReviewRepositoryImpl(
            reviewDao = database.reviewDao(),
            imageDao = database.imageDao(),
        )
    }
}