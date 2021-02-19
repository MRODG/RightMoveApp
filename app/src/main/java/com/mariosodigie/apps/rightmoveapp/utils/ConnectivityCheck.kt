package com.mariosodigie.apps.rightmoveapp.utils

import android.content.Context
import android.net.ConnectivityManager

class ConnectivityCheck(private val context: Context) {
    @Suppress("DEPRECATION")
    fun isConnectedToNetwork(): Boolean {
        return context.getSystemService(ConnectivityManager::class.java)?.activeNetworkInfo?.isConnected ?: false
    }
}