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
        val request = chain.request();
        if( !isInternetAvailable() ){
            onConnectionUnavailable( ConnectionErrorType.CONNECTION_IS_OFF );
        } else if( !isServerFound() ){
            onConnectionUnavailable( ConnectionErrorType.SERVER_NOT_FOUND )
        }
        return chain.proceed( request );
    }

}