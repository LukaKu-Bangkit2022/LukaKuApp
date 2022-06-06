package com.bangkit.capstone.lukaku.ui.result

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity
import com.bangkit.capstone.lukaku.data.repository.result.ResultRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ResultViewModel @Inject constructor(
    private val resultRepository: ResultRepository
) : ViewModel() {

    suspend fun insertDetection(result: DetectionEntity): Long {
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