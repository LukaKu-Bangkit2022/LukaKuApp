package com.bangkit.capstone.lukaku.data.models

class HospitalResponse : ArrayList<HospitalResponseItem>()

data class HospitalResponseItem(
    val latitude: String? = null,
    val longitude: String? = null,
    val nama: String? = null,
    val place: String? = null
)

data class HospitalBody(
    val latitude: String? = null,
    val longitude: String? = null
)