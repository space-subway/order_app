package com.online.booking.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Item (
    @SerializedName("id") var id : String,
    @SerializedName("title") var title : String,
    @SerializedName("description") var description : String,
    @SerializedName("descriptionShort") var descriptionShort : String,
    @SerializedName("price") var price : BigDecimal,
    @SerializedName("viewCount") var viewCount :  Int,
    @SerializedName("rating") var rating : Rating
) {
    public fun overrageRating() : Double {
        var overrageRating = 0.0
        if( rating != null ) {
            overrageRating = (( rating.oneStarCount + 2 * rating.twoStarCount + 3 * rating.threeStarCount + 4 * rating.fourStarCount + 5 * rating.fiveStarCount ) / 5).toDouble()
        }

        return overrageRating
    }
}