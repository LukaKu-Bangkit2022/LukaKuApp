package com.bangkit.capstone.lukaku.data.remote

import com.bangkit.capstone.lukaku.data.models.ArticleResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("getarticle")
    suspend fun getAllArticles(): ArticleResponse

    @GET("getarticle")
    suspend fun searchArticles(
        @Query("title")
        searchQuery: String
    ): ArticleResponse
}