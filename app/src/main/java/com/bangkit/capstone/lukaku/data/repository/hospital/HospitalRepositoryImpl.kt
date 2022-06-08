package com.bangkit.capstone.lukaku.data.repository.hospital

import com.bangkit.capstone.lukaku.data.models.HospitalBody
import com.bangkit.capstone.lukaku.data.models.HospitalResponse
import com.bangkit.capstone.lukaku.data.remote.ApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class HospitalRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : HospitalRepository {
    override fun getAllNearestHospital(hospitalBody: HospitalBody): Flow<Result<HospitalResponse>> = flow {
        try {
            val response = apiService.getNearestHospital(hospitalBody)
            emit(Result.success(response))
        } catch (exception: Exception) {
            emit(Result.failure(exception))
        }
    }
}