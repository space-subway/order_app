package com.online.booking.data.db

import androidx.room.*
import com.online.booking.data.model.Item

@Dao
interface ItemDao {
    @Query("SELECT * FROM item WHERE id = :id LIMIT 1")
    suspend fun findById( id : String ): Item?

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert( item: Item )

    @Update(onConflict = OnConflictStrategy.IGNORE)
    suspend fun update( item: Item )

    @Query("DELETE FROM item")
    suspend fun deleteAll()

    @get:Query("SELECT * FROM item ORDER BY title ASC")
    val allItems: List<Item>
}