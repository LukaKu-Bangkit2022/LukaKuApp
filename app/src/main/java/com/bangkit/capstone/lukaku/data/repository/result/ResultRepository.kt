package com.bangkit.capstone.lukaku.data.repository.result

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.local.entity.DetectionSaved
import com.bangkit.capstone.lukaku.data.local.room.DetectionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ResultRepository private constructor(
    private val detectionDao: DetectionDao
) {

    fun getDetectionSaved(uid: String): LiveData<List<DetectionSaved>> {
        return detectionDao.getDetectionSaved(uid)
    }

    suspend fun insertDetection(result: DetectionSaved): Long {
        return withContext(Dispatchers.IO) {
            detectionDao.saveDetection(result)
        }
    }

    suspend fun deleteFavorite(id: Long) {
        return withContext(Dispatchers.IO) {
            detectionDao.deleteDetection(id)
        }
    }

    companion object {
        @Volatile
        private var instance: ResultRepository? = null

        fun getInstance(
            usersDao: DetectionDao
        ): ResultRepository = instance ?: synchronized(this) {
            instance ?: ResultRepository(usersDao)
        }.also { instance = it }
    }
}