package com.example.brewkery.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [CartLineEntity::class, ActiveOrderEntity::class],
    version = 1,
    exportSchema = false
)
abstract class BrewDatabase : RoomDatabase() {
    abstract fun cartDao(): CartDao
    abstract fun orderDao(): OrderDao
}