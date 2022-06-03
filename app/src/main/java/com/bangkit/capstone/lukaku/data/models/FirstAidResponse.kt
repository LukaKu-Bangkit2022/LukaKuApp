package com.bangkit.capstone.lukaku.data.models

class FirstAidResponse : ArrayList<FirstAidResponseItem>()

data class FirstAidResponseItem(
    val firstaid: String,
    val id: Int?,
    val label: String
)