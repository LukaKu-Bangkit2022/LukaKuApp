package com.bangkit.capstone.lukaku.data.remote

import com.bangkit.capstone.lukaku.BuildConfig.BASE_URL_CF
import com.bangkit.capstone.lukaku.BuildConfig.DEBUG
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        private const val tesBaseURL = "https://story-api.dicoding.dev/v1/"

        private val level = if (DEBUG) Level.BODY else Level.NONE
        private val loggingInterceptor = HttpLoggingInterceptor().setLevel(level)
        private val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()

        fun getApiService(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL_CF)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }

        fun getApiService2(): ApiService2 {
            val retrofit = Retrofit.Builder()
//                .baseUrl(BASE_URL_PD)
                .baseUrl(tesBaseURL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService2::class.java)
        }
    }
}