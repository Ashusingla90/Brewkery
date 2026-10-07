package com.example.brewkery.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction

@Dao
abstract class CartDao {
    @Query("SELECT * FROM cart_lines ORDER BY position")
    abstract suspend fun getAll(): List<CartLineEntity>

    @Query("DELETE FROM cart_lines")
    abstract suspend fun clear()

    @Insert
    abstract suspend fun insertAll(lines: List<CartLineEntity>)

    @Transaction
    open suspend fun replaceAll(lines: List<CartLineEntity>) {
        clear()
        insertAll(lines)
    }
}

@Dao
interface OrderDao {
    @Query("SELECT * FROM active_order WHERE id = 1")
    suspend fun get(): ActiveOrderEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: ActiveOrderEntity)

    @Query("DELETE FROM active_order")
    suspend fun clear()
}