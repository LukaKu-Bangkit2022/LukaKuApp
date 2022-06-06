package com.bangkit.capstone.lukaku.ui.profile

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.repository.result.ResultRepository

class ProfileViewModel(
    private val resultRepository: ResultRepository
) : ViewModel() {

    fun getDetectionSaved(uid: String) = resultRepository.getDetectionSaved(uid)

    suspend fun deleteDetection(id: Long) {
        return resultRepository.deleteFavorite(id)
    }
}