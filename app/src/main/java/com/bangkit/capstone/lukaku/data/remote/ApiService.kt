package com.bangkit.capstone.lukaku.data.remote

import com.bangkit.capstone.lukaku.data.models.ArticleResponse
import com.bangkit.capstone.lukaku.data.models.DetectionResponseItem
import com.bangkit.capstone.lukaku.data.models.FirstAidResponse
import com.bangkit.capstone.lukaku.data.models.MedicineResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    @GET("getarticle")
    suspend fun getAllArticles(): ArticleResponse

    @GET("getfirstaid")
    suspend fun getFirstAids(
        @Query("label") name: String
    ): FirstAidResponse

    @GET("getmedicine")
    suspend fun getMedicine(
        @Query("label") name: String
    ): MedicineResponse
}

interface ApiService2 {
    @Multipart
    @POST("stories/guest")
    suspend fun detection(
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: Double? = null,
        @Part("lon") lon: Double? = null
    ): DetectionResponseItem
}