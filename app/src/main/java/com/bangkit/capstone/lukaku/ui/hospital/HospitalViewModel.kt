package com.bangkit.capstone.lukaku.ui.hospital

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.models.HospitalResponse
import com.bangkit.capstone.lukaku.data.repository.hospital.HospitalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val hospitalRepository: HospitalRepository
) : ViewModel() {
    fun getAllNearestHospital(requestBody: RequestBody): Flow<Result<HospitalResponse>> = hospitalRepository.getAllNearestHospital(requestBody)
}