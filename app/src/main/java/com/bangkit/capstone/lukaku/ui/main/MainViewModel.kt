package com.bangkit.capstone.lukaku.ui.main

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.repository.settings.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val settingsRepository: SettingsRepository
) : ViewModel() {
    fun getThemeSetting(): Flow<Boolean?> = settingsRepository.getThemeSetting()
}