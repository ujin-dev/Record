package com.yujin.record.app

import android.app.Application
import com.yujin.record.core.data.repository.ReviewRepository
import com.yujin.record.core.data.repository.ReviewRepositoryImpl
import com.yujin.record.core.database.AppDatabase
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class RecordApplication : Application()