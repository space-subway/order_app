package com.online.booking.data.api

import com.online.booking.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    private const val BASE_URL = "https://obscure-cove-65917.herokuapp.com"

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private fun buildRetrofit(): Retrofit {
        return if(BuildConfig.DEBUG){
            Retrofit.Builder()
                .baseUrl("http://10.0.2.2:8080")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        } else {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }
    }

    val apiService: ApiService = buildRetrofit().create(ApiService::class.java)
}