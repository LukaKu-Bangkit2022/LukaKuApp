package com.bangkit.capstone.lukaku.data.repository.hospital

import com.bangkit.capstone.lukaku.data.models.HospitalResponse
import com.bangkit.capstone.lukaku.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.RequestBody
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : HospitalRepository {
    override fun getAllNearestHospital(requestBody: RequestBody): Flow<Result<HospitalResponse>> = flow {
        try {
            val response = apiService.getNearestHospital(requestBody)
            emit(Result.success(response))
        } catch (exception: Exception) {
            emit(Result.failure(exception))
        }
    }
}