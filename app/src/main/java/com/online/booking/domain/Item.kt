package com.online.booking.domain

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class Item (
    @SerializedName("id") var id : String,
    @SerializedName("title") var title : String,
    @SerializedName("description") var description : String,
    @SerializedName("descriptionShort") var descriptionShort : String,
    @SerializedName("price") var price : BigDecimal,
    @SerializedName("viewCount") var viewCount :  Int,
    @SerializedName("rating") var rating : Rating,
    @SerializedName("category") var category: ItemCategory
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readString()!!,
        TODO("price"),
        parcel.readInt(),
        TODO("rating"),
        TODO("category")
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(id)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(descriptionShort)
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

}