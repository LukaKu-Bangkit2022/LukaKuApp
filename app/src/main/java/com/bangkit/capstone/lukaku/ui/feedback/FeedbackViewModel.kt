package com.bangkit.capstone.lukaku.ui.feedback

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.models.Feedback
import com.bangkit.capstone.lukaku.data.repository.drugstore.FeedbackRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val feedbackRepository: FeedbackRepository
) : ViewModel() {
    suspend fun insertFeedback(
        feedback: Feedback
    ) = feedbackRepository.insertFeedback(feedback)
}