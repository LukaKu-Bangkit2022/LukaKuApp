package com.bangkit.capstone.lukaku.data.remote

import com.bangkit.capstone.lukaku.data.models.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.*


interface ApiService {
    @GET("getarticle")
    suspend fun getAllArticles(): ArticleResponse

    @GET("getarticle")
    suspend fun searchArticles(
        @Query("title")
        searchQuery: String
    ): ArticleResponse

    @GET("getfirstaid")
    suspend fun getFirstAids(
        @Query("label") label: String
    ): FirstAidResponse? = null

    @GET("getmedicine")
    suspend fun getMedicine(
        @Query("label") label: String
    ): MedicineResponse? = null

    @Multipart
    @POST("predict")
    suspend fun detection(
        @Part file: MultipartBody.Part
    ): DetectionResponse

    @POST("insertfeedback")
    suspend fun inertFeedback(
        @Body feedback: Feedback
    ): FeedbackResponse
}