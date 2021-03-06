package com.online.booking.data.api

import com.online.booking.data.model.Item
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("item/list")
    suspend fun getItems(): List<Item>

    @GET("item/{id}")
    suspend fun getItem( @Path(value = "id") id: String?): Item

    @GET("item/{id}/view_cnt_inc")
    suspend fun viewCountInc( @Path(value = "id") id: String? ): Item

    @GET("item/{id}/rate/{rating}")
    suspend fun rate(
        @Path(value = "id") id: String?,
        @Path(value = "rating") rating: Int?
    ): Item

}