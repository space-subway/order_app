package com.online.booking.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.online.booking.data.model.Rating.CREATOR.ID
import com.online.booking.data.model.Rating.CREATOR.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = [ID], unique = true)]
)
data class Rating(
    @PrimaryKey
    @ColumnInfo(name = ID)
    var id: String,
    @SerializedName("OneStarCount") var oneStarCount    : Int,
    @SerializedName("TwoStarCount") var twoStarCount    : Int,
    @SerializedName("FreeStarCount") var threeStarCount  : Int,
    @SerializedName("FourStarCount") var fourStarCount   : Int,
    @SerializedName("FiveStarCount") var fiveStarCount   : Int
) : Parcelable {

    constructor(parcel : Parcel) : this(
        parcel.readString()!!,
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString( id )
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

        const val TABLE_NAME    = "rating"
        const val ID            = "id"
        //const val ONE_STARS     = "one_stars"
        //const val TWO_STARS     = "two_stars"
        //const val THREE_STARS   = "three_stars"
        //const val FOUR_STARS    = "four_stars"
        //const val FIVE_STARS    = "five_stars"

        override fun createFromParcel(parcel: Parcel): Rating {
            return Rating(parcel)
        }

        override fun newArray(size: Int): Array<Rating?> {
            return arrayOfNulls(size)
        }
    }
}
