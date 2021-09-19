package com.online.booking.web.utils

interface InternetConnectionListener {
    fun onInternetUnavailable()
    fun onServerIsNotAvailable()
    fun onServerResponse( code : Int )
}