package com.online.booking.data.api

class ApiHelper( private val apiService: ApiService ) {

    suspend fun getItems() = apiService.getItems()

    suspend fun getItem( id: String? ) = apiService.getItem( id )

}