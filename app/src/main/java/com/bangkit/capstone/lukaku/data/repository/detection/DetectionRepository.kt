package com.bangkit.capstone.lukaku.data.repository.detection

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import okhttp3.MultipartBody

interface DetectionRepository {
    suspend fun detection(photo: MultipartBody.Part): LiveData<Result<DetectionResult>>
}