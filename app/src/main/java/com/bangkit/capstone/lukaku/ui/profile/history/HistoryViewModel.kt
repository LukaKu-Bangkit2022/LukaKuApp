package com.bangkit.capstone.lukaku.ui.profile.history

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.bangkit.capstone.lukaku.data.repository.result.ResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val resultRepository: ResultRepository
) : ViewModel() {

    fun getDetectionSaved(uid: String) = resultRepository.getDetectionSaved(uid)

    fun deleteDetection(id: Long) {
        viewModelScope.launch {
            resultRepository.deleteFavorite(id)
        }
    }
}