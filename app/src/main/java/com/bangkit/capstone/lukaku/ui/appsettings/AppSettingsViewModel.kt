package com.bangkit.capstone.lukaku.ui.appsettings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.lukaku.data.repository.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AppSettingsViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingsRepository.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getThemeSetting(): Flow<Boolean?> = settingsRepository.getThemeSetting()
}