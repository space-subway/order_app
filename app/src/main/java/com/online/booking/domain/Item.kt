package com.online.booking.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Item (
    @SerializedName("id") var id : String,
    @SerializedName("tittle") var tittle : String,
    @SerializedName("description") var description : String,
    @SerializedName("price") var price : BigDecimal
)