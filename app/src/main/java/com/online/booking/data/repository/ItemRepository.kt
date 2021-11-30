package com.online.booking.data.repository

import com.online.booking.data.api.ItemService
import com.online.booking.data.model.Item
import retrofit2.Call

class ItemRepository(private val itemService : ItemService) {

    fun getItems(): Call<List<Item>> {
        return itemService.list()
    }

    fun getItem( id : String ): Call<Item> {
        return itemService.getItem( id )
    }

}