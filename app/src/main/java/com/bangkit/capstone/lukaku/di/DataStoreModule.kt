package com.bangkit.capstone.lukaku.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.bangkit.capstone.lukaku.data.local.preferences.OnboardingDataStore
import com.bangkit.capstone.lukaku.data.local.preferences.SettingsDataStore
import com.bangkit.capstone.lukaku.utils.Constants.DATASTORE_PREF
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

private val Context.dataStorePref: DataStore<Preferences> by preferencesDataStore(name = DATASTORE_PREF)

@Module
@InstallIn(SingletonComponent::class)
class DataStoreModule {
    @Singleton
    @Provides
    fun provideDataStore(@ApplicationContext context: Context): DataStore<Preferences> =
        context.dataStorePref

    @Singleton
    @Provides
    fun provideOnboardingDataStore(dataStore: DataStore<Preferences>): OnboardingDataStore =
        OnboardingDataStore(dataStore)

    @Singleton
    @Provides
    fun provideSettingsDataStore(dataStore: DataStore<Preferences>): SettingsDataStore =
        SettingsDataStore(dataStore)
}