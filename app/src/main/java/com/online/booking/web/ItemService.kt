package com.online.booking.web

import com.online.booking.domain.Item
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ItemService {
    @GET("item/list")
    fun list(): Call<List<Item>>
    @GET("item/{id}")
    fun getItem( @Path(value = "id") id: String?): Call<Item>
}