package com.bangkit.capstone.lukaku.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class FirstAidResponse : ArrayList<FirstAidResponseItem>(), Parcelable

data class FirstAidResponseItem(
    val firstaid: String? = null,
    val id: Int? = null,
    val label: String? = null
)