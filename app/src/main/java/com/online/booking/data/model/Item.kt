package com.online.booking.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import com.online.booking.data.model.Item.CREATOR.ID
import com.online.booking.data.model.Item.CREATOR.TABLE_NAME
import java.math.BigDecimal

@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = [ID], unique = true)]
)
data class Item(
    @SerializedName(ID) @PrimaryKey @ColumnInfo(name = ID) var id: String,
    @SerializedName(TITLE) @ColumnInfo(name = TITLE) var title: String?,
    @SerializedName(DESCRIPTION) @ColumnInfo(name = DESCRIPTION) var description: String?,
    @SerializedName(DESCRIPTION_SHORT) @ColumnInfo(name = DESCRIPTION_SHORT) var descriptionShort: String?,
    @SerializedName(PRICE) @ColumnInfo(name = PRICE) var price: BigDecimal,
    @SerializedName(VIEW_COUNT) @ColumnInfo(name = VIEW_COUNT) var viewCount:  Int,
    @SerializedName(RATING) @Embedded( prefix = "rating_" ) var rating: Rating?,
    @SerializedName(CATEGORY) @ColumnInfo(name = CATEGORY) var category: ItemCategory?
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
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

        const val TABLE_NAME        = "item"
        const val ID                = "id"
        const val TITLE             = "title"
        const val DESCRIPTION       = "description"
        const val DESCRIPTION_SHORT = "descriptionShort"
        const val PRICE             = "price"
        const val VIEW_COUNT        = "viewCount"
        const val RATING            = "rating"
        const val CATEGORY          = "category"

        override fun createFromParcel(parcel: Parcel): Item {
            return Item(parcel)
        }

        override fun newArray(size: Int): Array<Item?> {
            return arrayOfNulls(size)
        }
    }

     fun overageRating() : Double {
        var overageRating = 0.0
        if( rating != null ) {
            var divider = rating!!.oneStarCount + rating!!.twoStarCount + rating!!.threeStarCount + rating!!.fourStarCount + rating!!.fiveStarCount
            overageRating = if( divider == 0 ){
                0.0
            } else {
                (( rating!!.oneStarCount + 2 * rating!!.twoStarCount + 3 * rating!!.threeStarCount + 4 * rating!!.fourStarCount + 5 * rating!!.fiveStarCount ) / divider).toDouble()
            }
        }

        return overageRating
    }
}