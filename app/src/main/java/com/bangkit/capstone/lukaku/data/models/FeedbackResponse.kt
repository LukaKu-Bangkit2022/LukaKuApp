package com.bangkit.capstone.lukaku.data.models

import com.google.gson.annotations.SerializedName

data class FeedbackResponse(
    val result: String? = null
)

data class Feedback(
    @field:SerializedName("nama")
    val nama: String? = null,

    @field:SerializedName("text")
    val text: String? = null,

    @field:SerializedName("email")
    val email: String? = null
)

