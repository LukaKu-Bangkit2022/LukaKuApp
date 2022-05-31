package com.bangkit.capstone.lukaku.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class DetectionResponse : ArrayList<DetectionResponseItem>()

@Parcelize
data class DetectionResponseItem(
    val error: Boolean,
    val message: String
) : Parcelable

@Parcelize
data class DetectionResult(
    val detectionResponse: DetectionResponseItem,
    val firstAidResponse: FirstAidResponse? = null,
    val medicineResponse: MedicineResponse? = null
) : Parcelable