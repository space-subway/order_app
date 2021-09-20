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

    //abstract fun onCacheUnavailable()

    override fun intercept(chain: Interceptor.Chain): Response {

        var request = chain.request();
        if( !isInternetAvailable() ){
            onConnectionUnavailable( ConnectionErrorType.CONNECTION_IS_OFF );
        } else if( !isServerFound() ){
            onConnectionUnavailable( ConnectionErrorType.SERVER_NOT_FOUND )
        }

        if( !isInternetAvailable() || !isServerFound() ){
            //max-stale= 60 * 60 * 24
            //that means, we are allowing the response that is cached within the past 24 Hours from now.

            request = request.newBuilder().header("Cache-Control",
                "public, only-if-cached, max-stale=" + 60 * 60 * 24).build()

            //val response = chain.proceed( request )
            //if( response.cacheResponse() == null ){
            //    onCacheUnavailable()
            //}
            //return response

            return chain.proceed( request )
        }

        return chain.proceed( request )

    }

}