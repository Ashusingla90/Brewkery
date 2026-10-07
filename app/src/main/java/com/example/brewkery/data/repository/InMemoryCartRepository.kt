package com.example.brewkery.data.repository


import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.repository.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryCartRepository @Inject constructor() : CartRepository {

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    override val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    override fun add(cartItem: CartItem) {
        _items.update { current ->
            if (current.none { it.lineId == cartItem.lineId }) {
                current + cartItem
            } else {
                // Same item with identical choices: merge into one line
                current.map {
                    if (it.lineId == cartItem.lineId) it.copy(quantity = it.quantity + cartItem.quantity) else it
                }
            }
        }
    }

    override fun updateQuantity(lineId: String, quantity: Int) {
        _items.update { current ->
            if (quantity <= 0) current.filterNot { it.lineId == lineId }
            else current.map { if (it.lineId == lineId) it.copy(quantity = quantity) else it }
        }
    }

    override fun remove(lineId: String) {
        _items.update { current -> current.filterNot { it.lineId == lineId } }
    }

    override fun clear() {
        _items.value = emptyList()
    }
}