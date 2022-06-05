package com.bangkit.capstone.lukaku.data.local.room

import androidx.room.Database
import androidx.room.RoomDatabase
import com.bangkit.capstone.lukaku.data.local.entity.ArticleEntity

@Database(entities = [ArticleEntity::class], version = 1, exportSchema = false)
abstract class ArticleDatabase : RoomDatabase() {
    abstract fun articleDao(): ArticleDao
}