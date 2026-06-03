package com.yujin.record.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.sqlite.db.SupportSQLiteDatabase
import com.yujin.record.core.database.dao.ImageDao
import com.yujin.record.core.database.dao.ReviewDao
import com.yujin.record.core.database.model.ImageEntity
import com.yujin.record.core.database.model.ReviewEntity
import com.yujin.record.core.database.util.Converters

const val DATABASE_NAME = "record.db"

@Database(entities = [ReviewEntity::class, ImageEntity::class], version = 1, exportSchema = true)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun reviewDao(): ReviewDao
    abstract fun imageDao(): ImageDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                    // TODO use for debug only
                    .fallbackToDestructiveMigration(true)
                    .addCallback(object : Callback() {
                        override fun onOpen(db: SupportSQLiteDatabase) {
                            db.execSQL("PRAGMA foreign_keys=ON")
                        }
                    })
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}