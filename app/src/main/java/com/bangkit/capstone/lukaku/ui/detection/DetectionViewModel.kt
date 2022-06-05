package com.bangkit.capstone.lukaku.ui.detection

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.repository.detection.DetectionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import javax.inject.Inject

@HiltViewModel
class DetectionViewModel @Inject constructor(
    private val detectionRepository: DetectionRepository
) : ViewModel() {
    suspend fun detection(
        photo: MultipartBody.Part
    ) = detectionRepository.detection(photo)
}