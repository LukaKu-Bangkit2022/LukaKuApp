package com.bangkit.capstone.lukaku.data.repository.result

import androidx.lifecycle.LiveData
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity
import com.bangkit.capstone.lukaku.data.local.room.DetectionDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class ResultRepositoryImpl @Inject constructor(
    private val detectionDao: DetectionDao
) : ResultRepository {
    override fun getDetectionSaved(uid: String): LiveData<List<DetectionEntity>> {
        return detectionDao.getDetectionSaved(uid)
    }

    override suspend fun insertDetection(result: DetectionEntity): Long {
        return withContext(Dispatchers.IO) {
            detectionDao.saveDetection(result)
        }
    }

    override suspend fun deleteFavorite(id: Long) {
        return withContext(Dispatchers.IO) {
            detectionDao.deleteDetection(id)
        }
    }
}