package com.bangkit.capstone.lukaku.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DetectionResponse(
    val date: String? = null,
    val name: String? = null
) : Parcelable

@Parcelize
data class DetectionResult(
    val detectionResponse: DetectionResponse? = null,
    val firstAidResponse: FirstAidResponse? = null,
    val medicineResponse: MedicineResponse? = null
) : Parcelable
