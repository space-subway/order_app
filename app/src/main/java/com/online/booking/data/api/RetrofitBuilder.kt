package com.online.booking.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitBuilder {

    const val BASE_URL          = "https://obscure-cove-65917.herokuapp.com"
    //const val CACHE_DIR_NAME    = "cache"
    //const val DISK_CACHE_SIZE   = 10 * 1024 * 1024; // 10 MB

    private fun buildOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout( 30, TimeUnit.SECONDS )
            .readTimeout( 30, TimeUnit.SECONDS )
            .writeTimeout( 30, TimeUnit.SECONDS )
            /*.addInterceptor( object: NetworkConnectionInterceptor(){
                override fun isInternetAvailable(): Boolean{
                    return this@App.isInternetAvailable()
                }

                override fun isServerFound(): Boolean {
                    return this@App.isServerFound()
                }

                override fun onConnectionUnavailable( errType: ConnectionErrorType){
                    when (errType) {
                        ConnectionErrorType.CONNECTION_IS_OFF -> connectionListener?.onInternetUnavailable()
                        ConnectionErrorType.SERVER_NOT_FOUND -> connectionListener?.onServerIsNotAvailable()
                    }
                }

            } )
            .cache( getCache() )*/
            .build()
    }

    fun buildRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val apiService: ApiService = buildRetrofit().create( ApiService::class.java )
}