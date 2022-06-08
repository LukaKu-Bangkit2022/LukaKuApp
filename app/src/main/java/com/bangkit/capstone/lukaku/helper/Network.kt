package com.bangkit.capstone.lukaku.helper

import android.content.Context
import android.content.Context.CONNECTIVITY_SERVICE
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.os.Build

object Network {
    fun isConnect(context: Context): Boolean {
        val manager = context.getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = manager.activeNetwork ?: return false
            val capabilities = manager.getNetworkCapabilities(network) ?: return false

            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            val networkInfo = manager.activeNetworkInfo
            return networkInfo?.isConnected ?: false ||
                    networkInfo?.isConnectedOrConnecting ?: false
        }
    }
}