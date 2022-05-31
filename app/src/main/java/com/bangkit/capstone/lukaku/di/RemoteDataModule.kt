package com.bangkit.capstone.lukaku.di

import com.bangkit.capstone.lukaku.BuildConfig.BASE_URL_CF
import com.bangkit.capstone.lukaku.data.remote.*
import com.bangkit.capstone.lukaku.data.remote.ApiConfig.Companion.baseURLTest
import com.bangkit.capstone.lukaku.data.remote.ApiConfig.Companion.getApiService
import com.bangkit.capstone.lukaku.data.remote.ApiService
import com.bangkit.capstone.lukaku.data.remote.ApiService2
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
    fun provideApiService(): ApiService = getApiService(BASE_URL_CF)

    @Singleton
    @Provides
    fun provideApiService2(): ApiService2 = getApiService(baseURLTest)
}