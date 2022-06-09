package com.bangkit.capstone.lukaku.data.repository.drugstore

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.bangkit.capstone.lukaku.data.models.Feedback
import com.bangkit.capstone.lukaku.data.models.FeedbackResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

interface FeedbackRepository {
    suspend fun insertFeedback(feedback: Feedback): LiveData<Result<FeedbackResponse>>
}