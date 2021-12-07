package com.online.booking.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.online.booking.data.model.ItemCategory.CREATOR.ID
import com.online.booking.data.model.ItemCategory.CREATOR.TABLE_NAME

@Entity(
    tableName = TABLE_NAME,
    indices = [Index(value = [ID], unique = true)]
)
data class ItemCategory (
    @SerializedName("id") @PrimaryKey @ColumnInfo(name = ID) var id: String,
    @SerializedName("name") @ColumnInfo(name = NAME) var name: String
) : Parcelable {

    constructor( parcel: Parcel ) : this (
        parcel.readString()!!,
        parcel.readString()!!
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString( id )
        parcel.writeString( name )
    }

    companion object CREATOR : Parcelable.Creator<ItemCategory> {

        const val TABLE_NAME = "item_category"
        const val ID         = "id"
        const val NAME       = "name"

        override fun createFromParcel(parcel: Parcel): ItemCategory {
            return ItemCategory(parcel)
        }

        override fun newArray(size: Int): Array<ItemCategory?> {
            return arrayOfNulls(size)
        }
    }
}