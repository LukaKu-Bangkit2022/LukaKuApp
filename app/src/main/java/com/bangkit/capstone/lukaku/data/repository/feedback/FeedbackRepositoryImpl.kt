package com.bangkit.capstone.lukaku.data.repository.feedback

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.bangkit.capstone.lukaku.data.models.Feedback
import com.bangkit.capstone.lukaku.data.models.FeedbackResponse
import com.bangkit.capstone.lukaku.data.remote.ApiService
import javax.inject.Inject

class FeedbackRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : FeedbackRepository {
    override suspend fun insertFeedback(feedback: Feedback): LiveData<Result<FeedbackResponse>> =
        liveData {
            try {
                val detectionResponse = apiService.inertFeedback(feedback)
                emit(Result.success(detectionResponse))
            } catch (exception: Exception) {
                exception.printStackTrace()
                emit(Result.failure(exception))
            }
        }
}