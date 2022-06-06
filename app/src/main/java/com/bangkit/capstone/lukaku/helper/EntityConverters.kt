package com.bangkit.capstone.lukaku.helper

import androidx.room.TypeConverter
import com.bangkit.capstone.lukaku.data.models.DetectionResult
import com.google.gson.Gson

class EntityConverters {
    @TypeConverter
    fun resultDetectionToString(value: DetectionResult?): String? {
        val gson = Gson()
        return value?.let { gson.toJson(it) }
    }

    @TypeConverter
    fun stringToResultDetection(value: String?): DetectionResult? {
        val gson = Gson()
        return value?.let { gson.fromJson(it, DetectionResult::class.java) }
    }
}