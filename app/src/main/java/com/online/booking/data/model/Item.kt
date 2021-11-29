package com.online.booking.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Item(
    @SerializedName("id") var id: String?,
    @SerializedName("title") var title: String?,
    @SerializedName("description") var description: String?,
    @SerializedName("descriptionShort") var descriptionShort: String?,
    @SerializedName("price") var price: BigDecimal,
    @SerializedName("viewCount") var viewCount:  Int,
    @SerializedName("rating") var rating: Rating?,
    @SerializedName("category") var category: ItemCategory?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        BigDecimal.valueOf(parcel.readDouble()),
        parcel.readInt(),
        parcel.readParcelable(Rating.javaClass.classLoader),
        parcel.readParcelable(ItemCategory.javaClass.classLoader)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(descriptionShort)
        parcel.writeDouble(price.toDouble())
        parcel.writeInt(viewCount)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Item> {
        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }

     fun overrageRating() : Double {
        var overrageRating = 0.0
        if( rating != null ) {
            var divider = rating!!.oneStarCount + rating!!.twoStarCount + rating!!.threeStarCount + rating!!.fourStarCount + rating!!.fiveStarCount
            if( divider == 0 ){
                overrageRating = 0.0
            } else {
                overrageRating = (( rating!!.oneStarCount + 2 * rating!!.twoStarCount + 3 * rating!!.threeStarCount + 4 * rating!!.fourStarCount + 5 * rating!!.fiveStarCount ) / divider).toDouble()
            }
        }

        return overrageRating
    }
}