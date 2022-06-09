package com.bangkit.capstone.lukaku.ui.hospital

import androidx.lifecycle.ViewModel
import com.bangkit.capstone.lukaku.data.models.HospitalBody
import com.bangkit.capstone.lukaku.data.models.HospitalResponse
import com.bangkit.capstone.lukaku.data.repository.hospital.HospitalRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class HospitalViewModel @Inject constructor(
    private val hospitalRepository: HospitalRepository
) : ViewModel() {
    fun getAllNearestHospital(hospitalBody: HospitalBody): Flow<Result<HospitalResponse>> =
        hospitalRepository.getAllNearestHospital(hospitalBody)
}