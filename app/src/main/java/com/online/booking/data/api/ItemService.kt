package com.online.booking.data.api

import com.online.booking.data.model.Item
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemService {
    @GET("item/list")
    fun list(): Call<List<Item>>
    @GET("item/{id}")
    fun getItem( @Path(value = "id") id: String?): Call<Item>
}