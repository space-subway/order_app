package com.online.booking.utils

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.online.booking.data.model.ItemCategory
import com.online.booking.data.model.Rating
import java.math.BigDecimal

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromBigDecimal( value: BigDecimal? ): Double? {
            return value!!.toDouble()
        }

        @TypeConverter
        @JvmStatic
        fun toBigDecimal( value: Double? ): BigDecimal? {
            return BigDecimal.valueOf( value!! )
        }

        @TypeConverter
        @JvmStatic
        fun fromItemCategory( value: ItemCategory? ): String? {
            val type = object: TypeToken<ItemCategory>(){}.type
            return Gson().toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toItemCategory( value: String? ): ItemCategory? {
            val type = object: TypeToken<ItemCategory>(){}.type
            return Gson().fromJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun fromRating( value: Rating? ): String? {
            val type = object: TypeToken<Rating>(){}.type
            return Gson().toJson(value, type)
        }

        @TypeConverter
        @JvmStatic
        fun toRating( value: String? ): Rating? {
            val type = object: TypeToken<Rating>(){}.type
            return Gson().fromJson( value, type )
        }

    }
}