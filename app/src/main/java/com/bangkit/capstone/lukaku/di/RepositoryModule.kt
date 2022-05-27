package com.bangkit.capstone.lukaku.di

import com.bangkit.capstone.lukaku.data.repository.article.ArticleRepository
import com.bangkit.capstone.lukaku.data.repository.article.ArticleRepositoryImpl
import com.bangkit.capstone.lukaku.data.repository.onboarding.OnboardingRepository
import com.bangkit.capstone.lukaku.data.repository.onboarding.OnboardingRepositoryImpl
import com.bangkit.capstone.lukaku.data.repository.settings.SettingsRepository
import com.bangkit.capstone.lukaku.data.repository.settings.SettingsRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    abstract fun bindsOnboardingRepository(onboardingRepositoryImpl: OnboardingRepositoryImpl): OnboardingRepository

    @Binds
    abstract fun bindsSettingsRepository(settingsRepositoryImpl: SettingsRepositoryImpl): SettingsRepository

    @Binds
    abstract fun bindsArticleRepository(articleRepositoryImpl: ArticleRepositoryImpl): ArticleRepository
}