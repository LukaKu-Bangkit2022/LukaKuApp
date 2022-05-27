package com.bangkit.capstone.lukaku.data.models

class ArticleResponse : ArrayList<ArticleResponseItem>()

data class ArticleResponseItem(
    val author: String,
    val category: String,
    val definition: String,
    val definition_description: String,
    val id: Int,
    val imageUrl: String,
    val publishedAt: String,
    val symptom: String,
    val symptom_description: String,
    val title: String,
    val treatment: String,
    val treatment_description: String
)
