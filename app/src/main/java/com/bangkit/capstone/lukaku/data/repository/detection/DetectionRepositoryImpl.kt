package com.bangkit.capstone.lukaku.data.repository.detection

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.data.remote.ApiService
import okhttp3.MultipartBody
import javax.inject.Inject

class DetectionRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : DetectionRepository {
    override suspend fun detection(
        photo: MultipartBody.Part
    ): LiveData<Result<DetectionResult>> = liveData {
        try {
            val detectionResponse = apiService.detection(photo)
            val label = detectionResponse.name
            if (label != null) {
                val firstAidResponse = apiService.getFirstAids(label)
                val medicineResponse = apiService.getMedicine(label)
                val detectionResult = DetectionResult(
                    detectionResponse,
                    firstAidResponse,
                    medicineResponse
                )
                emit(Result.success(detectionResult))
            } else {
                emit(
                    Result.success(
                        DetectionResult(null, null, null)
                    )
                )
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }
}