package com.online.booking.data.model

/*data class Rating(
    val oneStarCount    : Int,
    val twoStarCount    : Int,
    val threeStarCount  : Int,
    val fourStarCount   : Int,
    val fiveStarCount   : Int
)*/

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.online.booking.data.model.Rating.CREATOR.ID
import com.online.booking.data.model.Rating.CREATOR.TABLE_NAME

data class Rating(
    @SerializedName("oneStarCount") var oneStarCount    : Int,
    @SerializedName("twoStarCount") var twoStarCount    : Int,
    @SerializedName("threeStarCount") var threeStarCount  : Int,
    @SerializedName("fourStarCount") var fourStarCount   : Int,
    @SerializedName("fiveStarCount") var fiveStarCount   : Int
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

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Rating

        //if (id != other.id) return false
        if (oneStarCount != other.oneStarCount) return false
        if (twoStarCount != other.twoStarCount) return false
        if (threeStarCount != other.threeStarCount) return false
        if (fourStarCount != other.fourStarCount) return false
        if (fiveStarCount != other.fiveStarCount) return false

        return true
    }

    override fun hashCode(): Int {
        //var result = id.hashCode()
        var result = oneStarCount.hashCode()
        result = 31 * result + twoStarCount
        result = 31 * result + threeStarCount
        result = 31 * result + fourStarCount
        result = 31 * result + fiveStarCount
        return result
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
