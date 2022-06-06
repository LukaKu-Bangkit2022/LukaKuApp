package com.bangkit.capstone.lukaku.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity
import com.bangkit.capstone.lukaku.helper.EntityConverters
import com.bangkit.capstone.lukaku.utils.Constants.DB_VERSION

@Database(
    entities = [DetectionEntity::class, ArticleEntity::class],
    version = DB_VERSION,
    exportSchema = false
)
@TypeConverters(EntityConverters::class)
abstract class LukakuDatabase : RoomDatabase() {
    abstract fun detectionDao(): DetectionDao
    abstract fun articleDao(): ArticleDao
}