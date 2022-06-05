package com.bangkit.capstone.lukaku.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

class ArticleResponse : ArrayList<ArticleResponseItem>()

@Parcelize
data class ArticleResponseItem(
    val author: String,
    val category: String,
    val definition_description: String,
    val definition_title: String,
    val id: Int,
    val imageUrl: String,
    val publishedAt: String,
    val symptom_description: String,
    val symptom_title: String,
    val title: String,
    val treatment_description: String,
    val treatment_title: String
) : Parcelable
