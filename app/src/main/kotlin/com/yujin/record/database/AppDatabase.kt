package com.yujin.record.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.yujin.record.database.dao.ImageDao
import com.yujin.record.database.dao.ReviewDao
import com.yujin.record.database.model.ImageEntity
import com.yujin.record.database.model.ReviewEntity
import com.yujin.record.database.util.Converters

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