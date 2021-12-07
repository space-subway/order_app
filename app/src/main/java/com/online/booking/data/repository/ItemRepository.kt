package com.online.booking.data.repository

import com.online.booking.data.api.ApiHelper

class ItemRepository(private val apiHelper: ApiHelper ) {

    suspend fun getItems() = apiHelper.getItems()

    suspend fun getItem( id: String? ) = apiHelper.getItem( id )

}