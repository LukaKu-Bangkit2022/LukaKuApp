package com.bangkit.capstone.lukaku.data.remote

import com.bangkit.capstone.lukaku.data.models.ArticleResponse
import retrofit2.http.GET

interface ApiService {
    @GET("getarticle")
    suspend fun getAllArticles(): ArticleResponse
}