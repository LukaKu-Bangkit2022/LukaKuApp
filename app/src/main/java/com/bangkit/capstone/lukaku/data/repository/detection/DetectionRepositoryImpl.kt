package com.bangkit.capstone.lukaku.data.repository.detection

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.data.remote.ApiService
import com.bangkit.capstone.lukaku.data.remote.ApiService2
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class DetectionRepositoryImpl @Inject constructor(
    private val apiService: ApiService,
    private val apiService2: ApiService2
) : DetectionRepository {
    override suspend fun detection(
        photo: MultipartBody.Part,
        description: RequestBody
    ): LiveData<Result<DetectionResult>> = liveData {
        try {
            val detectionResponse = apiService2.detection(photo, description)

            if (!detectionResponse.error) {
//                val label = response.label
                val label = "burns"

                val firstAidResponse = apiService.getFirstAids(label)
                val medicineResponse = apiService.getMedicine(label)
                val detectionResult = DetectionResult(
                    detectionResponse,
                    firstAidResponse,
                    medicineResponse
                )
                emit(Result.success(detectionResult))
            }
        } catch (exception: Exception) {
            exception.printStackTrace()
            emit(Result.failure(exception))
        }
    }
}