package com.example.brewkery.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cart_lines")
data class CartLineEntity(
    @PrimaryKey val lineId: String,
    val position: Int,
    val json: String
)

@Entity(tableName = "active_order")
data class ActiveOrderEntity(
    @PrimaryKey val id: Int = 1,
    val json: String
)