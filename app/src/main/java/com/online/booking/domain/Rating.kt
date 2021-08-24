package com.online.booking.domain

import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("OneStarCount")     var oneStarCount    : Int,
    @SerializedName("TwoStarCount")     var twoStarCount    : Int,
    @SerializedName("FreeStarCount")    var threeStarCount  : Int,
    @SerializedName("FourStarCount")    var fourStarCount   : Int,
    @SerializedName("FiveStarCount")    var fiveStarCount   : Int
)
