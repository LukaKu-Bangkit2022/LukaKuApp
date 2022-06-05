package com.bangkit.capstone.lukaku.data.models

class FirstAidResponse : ArrayList<FirstAidResponseItem>()

data class FirstAidResponseItem(
    val firstaid: String? = null,
    val id: Int? = null,
    val label: String? = null
)