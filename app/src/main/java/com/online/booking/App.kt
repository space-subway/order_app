package com.online.booking

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.online.booking.data.api.ApiService
import com.online.booking.utils.InternetConnectionListener
import com.online.booking.utils.NetworkConnectionInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.util.concurrent.TimeUnit

class App : Application() {

    /*companion object {
        const val BASE_URL          = "https://obscure-cove-65917.herokuapp.com"
        const val CACHE_DIR_NAME    = "cache"
        const val DISK_CACHE_SIZE   = 10 * 1024 * 1024; // 10 MB
    }

    private fun buildOkHttpClient() : OkHttpClient {
        return OkHttpClient.Builder()
            .connectTimeout( 30, TimeUnit.SECONDS )
            .readTimeout( 30, TimeUnit.SECONDS )
            .writeTimeout( 30, TimeUnit.SECONDS )
            .addInterceptor( object: NetworkConnectionInterceptor(){
                override fun isInternetAvailable(): Boolean{
                    return this@App.isInternetAvailable()
                }

                override fun isServerFound(): Boolean {
                    return this@App.isServerFound()
                }

                override fun onConnectionUnavailable( errType: ConnectionErrorType ){
                    when (errType) {
                        ConnectionErrorType.CONNECTION_IS_OFF -> connectionListener?.onInternetUnavailable()
                        ConnectionErrorType.SERVER_NOT_FOUND -> connectionListener?.onServerIsNotAvailable()
                    }
                }

            } )
            .cache( getCache() )
            .build()
    }

    fun buildRetrofit() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(buildOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    var connectionListener: InternetConnectionListener? = null

    private fun isInternetAvailable(): Boolean {
        val connectivityManager = getSystemService( Context.CONNECTIVITY_SERVICE ) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw      = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                //for other device how are able to connect with Ethernet
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                //for check internet over Bluetooth
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val nwInfo = connectivityManager.activeNetworkInfo ?: return false
            return nwInfo.isConnected
        }
    }

    private fun isServerFound(): Boolean {
        var exist = false

        val ip = BASE_URL.split(":")[1].removePrefix("//")
        var port = 80
        if( BASE_URL.split(":").size > 2 ){
            port = BASE_URL.split(":")[2].removeSuffix("/").toInt()
        }

        try {
            val sockaddr = InetSocketAddress(ip, port)
            val socket = Socket()
            socket.connect( sockaddr, 2000 )

            exist = true
        } catch (e: IOException) {
            e.printStackTrace()
        }

        return exist
    }

    private fun getCache(): Cache {
        val cacheDir = File( cacheDir, CACHE_DIR_NAME )
        return Cache( cacheDir, DISK_CACHE_SIZE.toLong())
    }

    val apiService: ApiService = buildRetrofit().create(ApiService::class.java)*/
}