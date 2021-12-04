package com.online.booking.utils

interface InternetConnectionListener {
    fun onInternetUnavailable()
    fun onServerIsNotAvailable()
    fun onServerResponse( code : Int )
    fun onNetworkError(message: String?)
}