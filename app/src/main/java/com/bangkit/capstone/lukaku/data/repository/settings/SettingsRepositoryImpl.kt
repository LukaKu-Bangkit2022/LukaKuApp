package com.bangkit.capstone.lukaku.data.repository.settings

import com.bangkit.capstone.lukaku.data.local.preferences.SettingsDataStore
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    private val settingsDataStore: SettingsDataStore
) : SettingsRepository {
    override suspend fun saveThemeSetting(isDarkModeActive: Boolean) =
        settingsDataStore.saveThemeSetting(isDarkModeActive)

    override fun getThemeSetting(): Flow<Boolean?> = settingsDataStore.getThemeSetting()
}