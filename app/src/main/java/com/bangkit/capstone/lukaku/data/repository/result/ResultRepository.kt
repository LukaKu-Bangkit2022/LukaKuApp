package com.bangkit.capstone.lukaku.data.repository.result

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity

interface ResultRepository {
    fun getDetectionSaved(uid: String): LiveData<List<DetectionEntity>>
    suspend fun insertDetection(result: DetectionEntity): Long
    suspend fun deleteFavorite(id: Long)
}