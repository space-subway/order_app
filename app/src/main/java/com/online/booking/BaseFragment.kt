package com.online.booking

import androidx.fragment.app.Fragment
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

open class BaseFragment : Fragment() {
    public final val BASE_URL = "http://10.0.2.2:8080/order_online/"

    protected val retrofit : Retrofit =
        Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}