package com.bangkit.capstone.lukaku.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class MedicineResponse : ArrayList<MedicineResponseItem>()

@Parcelize
data class MedicineResponseItem(
    val price: String? = null,
    val imageUrl: String? = null,
    val name: String? = null,
    val description: String? = null,
    val id: Int? = null,
    val label: String? = null
) : Parcelable