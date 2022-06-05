package com.bangkit.capstone.lukaku.data.local.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bangkit.capstone.lukaku.data.local.entity.DetectionSaved
import com.bangkit.capstone.lukaku.helper.EntityConverters
import com.bangkit.capstone.lukaku.utils.Constants.DB_NAME
import com.bangkit.capstone.lukaku.utils.Constants.DB_VERSION

@Database(entities = [DetectionSaved::class], version = DB_VERSION, exportSchema = false)
@TypeConverters(EntityConverters::class)
abstract class DetectionDatabase : RoomDatabase() {
    abstract fun detectionDao(): DetectionDao

    companion object {
        @Volatile
        private var instance: DetectionDatabase? = null
        fun getInstance(context: Context): DetectionDatabase =
            instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    DetectionDatabase::class.java, DB_NAME
                ).build()
            }
    }
}