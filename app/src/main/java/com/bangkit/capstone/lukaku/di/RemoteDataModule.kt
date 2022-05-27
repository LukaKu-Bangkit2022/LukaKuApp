package com.bangkit.capstone.lukaku.di

import com.bangkit.capstone.lukaku.data.remote.ApiConfig
import com.bangkit.capstone.lukaku.data.remote.ApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RemoteDataModule {
    @Singleton
    @Provides
    fun provideApiService(): ApiService = ApiConfig.getApiService()
}