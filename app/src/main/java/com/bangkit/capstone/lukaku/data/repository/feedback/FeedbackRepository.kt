package com.bangkit.capstone.lukaku.data.repository.feedback

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.models.Feedback
import com.bangkit.capstone.lukaku.data.models.FeedbackResponse

interface FeedbackRepository {
    suspend fun insertFeedback(feedback: Feedback): LiveData<Result<FeedbackResponse>>
}