package com.bangkit.capstone.lukaku.di

import android.content.Context
import com.bangkit.capstone.lukaku.data.local.room.DetectionDatabase
import com.bangkit.capstone.lukaku.data.remote.ApiConfig
import com.bangkit.capstone.lukaku.data.repository.result.ResultRepository

object Injection {
    inline fun <reified T> provideRepository(context: Context): T? {
        val database = DetectionDatabase.getInstance(context)
        val dao = database.detectionDao()

        return when (T::class.java) {
            ResultRepository::class.java -> ResultRepository.getInstance(dao) as T
            else -> null
        }
    }
}