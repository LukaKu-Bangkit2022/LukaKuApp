package com.bangkit.capstone.lukaku.data.models

class HospitalResponse : ArrayList<HospitalResponseItem>()

data class HospitalResponseItem(
    val latitude: String,
    val longitude: String,
    val nama: String
)