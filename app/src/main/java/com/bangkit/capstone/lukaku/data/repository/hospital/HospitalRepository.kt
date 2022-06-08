package com.bangkit.capstone.lukaku.data.repository.hospital

import com.bangkit.capstone.lukaku.data.models.HospitalBody
import com.bangkit.capstone.lukaku.data.models.HospitalResponse
import kotlinx.coroutines.flow.Flow

interface HospitalRepository {
    fun getAllNearestHospital(hospitalBody: HospitalBody): Flow<Result<HospitalResponse>>
}