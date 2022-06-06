package com.bangkit.capstone.lukaku.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bangkit.capstone.lukaku.data.local.entity.DetectionEntity

@Dao
interface DetectionDao {
    @Query("SELECT * FROM detection WHERE uid = :uid ORDER BY id")
    fun getDetectionSaved(uid: String): LiveData<List<DetectionEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveDetection(result: DetectionEntity): Long

    @Query("DELETE FROM detection WHERE id = :id")
    suspend fun deleteDetection(id: Long)

    @Query("DELETE FROM detection")
    suspend fun deleteDetection()
}