package com.mariosodigie.apps.rightmoveapp.averageprices.api

import com.mariosodigie.apps.rightmoveapp.BuildConfig
import com.mariosodigie.apps.rightmoveapp.averageprices.adapters.PricesJsonAdapter
import com.squareup.moshi.Moshi
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET


interface ApiService {
    @GET("Code-Challenge-Android/master/properties.json")
    fun requestPropertyPrices(): Call<List<Int>>

    companion object {
        fun getBaseURL(): String {
            return BuildConfig.AVERAGE_PRICES_FEATURE_BASE_URL
        }

        fun get(): ApiService {
            val moshi = Moshi.Builder().add(PricesJsonAdapter())
                .build()

            return Retrofit.Builder()
                .baseUrl(BuildConfig.AVERAGE_PRICES_FEATURE_BASE_URL)
                .addConverterFactory(MoshiConverterFactory.create(moshi))
                .build().create(ApiService::class.java)

        }
    }
}