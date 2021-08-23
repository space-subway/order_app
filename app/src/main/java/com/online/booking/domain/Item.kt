package com.online.booking.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Item (
    @SerializedName("id") var id : String,
    @SerializedName("title") var title : String,
    @SerializedName("description") var description : String,
    @SerializedName("descriptionShort") var descriptionShort : String,
    @SerializedName("price") var price : BigDecimal,
    @SerializedName("viewCount") var viewCount :  Integer,
    @SerializedName("rating") var rating : Float
)