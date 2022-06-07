package com.bangkit.capstone.lukaku.data.repository.hospital

import com.bangkit.capstone.lukaku.data.models.HospitalResponse
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody

interface HospitalRepository {
    fun getAllNearestHospital(requestBody: RequestBody): Flow<Result<HospitalResponse>>
}