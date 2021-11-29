package com.online.booking.data.model

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Rating(
    @SerializedName("OneStarCount")     var oneStarCount    : Int,
    @SerializedName("TwoStarCount")     var twoStarCount    : Int,
    @SerializedName("FreeStarCount")    var threeStarCount  : Int,
    @SerializedName("FourStarCount")    var fourStarCount   : Int,
    @SerializedName("FiveStarCount")    var fiveStarCount   : Int
) : Parcelable {

    constructor(parcel : Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt( oneStarCount )
        parcel.writeInt( twoStarCount )
        parcel.writeInt( threeStarCount )
        parcel.writeInt( fourStarCount )
        parcel.writeInt( fiveStarCount )
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Rating>{
        override fun createFromParcel(parcel: Parcel): Rating {
            return Rating(parcel)
        }

        override fun newArray(size: Int): Array<Rating?> {
            return arrayOfNulls(size)
        }
    }
}
