package com.bangkit.capstone.lukaku.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.local.entity.DetectionSaved
import com.bangkit.capstone.lukaku.data.repository.result.ResultRepository

class ResultViewModel(
    private val resultRepository: ResultRepository
) : ViewModel() {

    suspend fun insertDetection(result: DetectionSaved): Long {
        return resultRepository.insertDetection(result)
    }

    suspend fun deleteDetection(id: Long) {
        return resultRepository.deleteFavorite(id)
    }

    val mutableIsSave: MutableLiveData<Boolean> = MutableLiveData<Boolean>()

    val mutableResultId: MutableLiveData<Long> = MutableLiveData<Long>()

    fun isSave(): LiveData<Boolean> = mutableIsSave

    fun getResultId(): LiveData<Long> = mutableResultId
}