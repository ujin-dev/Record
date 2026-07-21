package com.yujin.record.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yujin.record.core.database.dao.ImageDao
import com.yujin.record.core.database.dao.ReviewDao
import com.yujin.record.core.database.model.ImageEntity
import com.yujin.record.core.database.model.ReviewEntity
import com.yujin.record.core.database.util.Converters

@Database(
    entities = [ReviewEntity::class, ImageEntity::class],
    version = 1,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun imageDao(): ImageDao
}