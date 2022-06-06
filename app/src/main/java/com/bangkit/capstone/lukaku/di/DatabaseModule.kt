package com.bangkit.capstone.lukaku.di

import android.content.Context
import androidx.room.Room
import com.bangkit.capstone.lukaku.data.local.room.ArticleDao
import com.bangkit.capstone.lukaku.data.local.room.DetectionDao
import com.bangkit.capstone.lukaku.data.local.room.LukakuDatabase
import com.bangkit.capstone.lukaku.utils.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DatabaseModule {
    @Singleton
    @Provides
    fun provideArticleDatabase(@ApplicationContext context: Context): LukakuDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            LukakuDatabase::class.java,
            DB_NAME
        ).build()

    @Singleton
    @Provides
    fun provideDetectionDao(database: LukakuDatabase): DetectionDao =
        database.detectionDao()

    @Singleton
    @Provides
    fun provideArticleDao(database: LukakuDatabase): ArticleDao =
        database.articleDao()
}