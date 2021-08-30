package com.online.booking.domain

import com.google.gson.annotations.SerializedName

data class ItemCategory (
    @SerializedName("id") var id: String,
    @SerializedName("name") var name: String
)