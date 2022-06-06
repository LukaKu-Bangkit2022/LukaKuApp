package com.bangkit.capstone.lukaku.ui.profile.history

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.repository.result.ResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val resultRepository: ResultRepository
) : ViewModel() {

    fun getDetectionSaved(uid: String) = resultRepository.getDetectionSaved(uid)

    suspend fun deleteDetection(id: Long) {
        return resultRepository.deleteFavorite(id)
    }
}