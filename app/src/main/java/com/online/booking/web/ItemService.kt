package com.online.booking.web

import com.online.booking.domain.Item
import retrofit2.Call
import retrofit2.http.GET

interface ItemService {
    @GET("product/list")
    fun list(): Call<List<Item>>
}