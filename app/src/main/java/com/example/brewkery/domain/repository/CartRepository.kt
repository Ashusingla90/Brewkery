package com.example.brewkery.domain.repository

import com.example.brewkery.domain.model.CartItem
import kotlinx.coroutines.flow.StateFlow

interface CartRepository {
    val items: StateFlow<List<CartItem>>
    fun add(cartItem: CartItem)
    fun updateQuantity(lineId: String, quantity: Int)
    fun remove(lineId: String)
    fun clear()
}