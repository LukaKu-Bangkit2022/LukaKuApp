package com.bangkit.capstone.lukaku.data.repository.detection

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.models.DetectionResponseItem
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.data.models.FirstAidResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface DetectionRepository {
    suspend fun detection(
        photo: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<DetectionResult>>
}