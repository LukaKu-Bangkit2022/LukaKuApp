package com.bangkit.capstone.lukaku.data.models

class MedicineResponse : ArrayList<MedicineResponseItem>()

data class MedicineResponseItem(
    val price: String? = null,
    val imageUrl: String? = null,
    val name: String,
    val description: String? = null,
    val id: Int,
    val label: String
)