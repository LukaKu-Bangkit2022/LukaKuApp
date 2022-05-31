package com.bangkit.capstone.lukaku.data.remote

import me.ibrahimsn.lib.BuildConfig.DEBUG
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import okhttp3.logging.HttpLoggingInterceptor.Level
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiConfig {
    companion object {
        const val baseURLTest = "https://story-api.dicoding.dev/v1/"

        inline fun <reified T> getApiService(baseUrl: String): T {
            val level = if (DEBUG) Level.BODY else Level.NONE
            val loggingInterceptor = HttpLoggingInterceptor().setLevel(level)

            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(T::class.java)
        }
    }
}