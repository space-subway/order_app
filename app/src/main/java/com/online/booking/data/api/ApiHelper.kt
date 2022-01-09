package com.online.booking.data.api

class ApiHelper( private val apiService: ApiService ) {

    suspend fun getItems() = apiService.getItems()

    suspend fun getItem( id: String? ) = apiService.getItem( id )

    suspend fun viewCountInc( id: String? ) = apiService.viewCountInc( id )

    suspend fun rate( id: String?, rating: Int? ) = apiService.rate( id, rating )
}