package com.bangkit.capstone.lukaku.ui.feedback

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.models.Feedback
import com.bangkit.capstone.lukaku.data.repository.drugstore.DrugstoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class FeedbackViewModel @Inject constructor(
    private val drugstoreRepository: DrugstoreRepository
) : ViewModel() {
    suspend fun insertFeedback(
        feedback: Feedback
    ) = drugstoreRepository.insertFeedback(feedback)
}