package com.online.booking.web.utils

import okhttp3.Interceptor
import okhttp3.Response

abstract class NetworkConnectionInterceptor : Interceptor {

    enum class ConnectionErrorType {
        CONNECTION_IS_OFF, SERVER_NOT_FOUND
    }

    abstract fun isInternetAvailable(): Boolean

    abstract fun isServerFound(): Boolean

    abstract fun onConnectionUnavailable( errType: ConnectionErrorType )

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request()
        //max-stale= 60 * 60 * 24
        //that means, we are allowing the response that is cached within the past 24 Hours from now.
        var cacheRequest = request.newBuilder().header("Cache-Control",
            "public, only-if-cached, max-stale=" + 60 * 60 * 24).build()

        val response = chain.proceed( cacheRequest )

        if( response.cacheResponse() != null ) {
            return if( isInternetAvailable() && isServerFound() ) {
                chain.proceed( request )
            } else {
                response
            }
        }

        if( !isInternetAvailable() ){
            onConnectionUnavailable( ConnectionErrorType.CONNECTION_IS_OFF );
        } else if( !isServerFound() ){
            onConnectionUnavailable( ConnectionErrorType.SERVER_NOT_FOUND )
        }

        return chain.proceed( request )

    }

}