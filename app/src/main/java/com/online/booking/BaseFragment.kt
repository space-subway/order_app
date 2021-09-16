package com.online.booking

import androidx.fragment.app.Fragment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseFragment : Fragment() {
    public final val BASE_URL = "http://10.0.2.2:8080/"

    protected val retrofit : Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    protected val okHttpClient : OkHttpClient =
        OkHttpClient.Builder()
            .connectTimeout( 30, TimeUnit.SECONDS )
            .readTimeout( 30, TimeUnit.SECONDS )
            .writeTimeout( 30, TimeUnit.SECONDS )
            .build()
}