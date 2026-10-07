package com.example.brewkery.presentation.detail

import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.model.MenuItem
import com.example.brewkery.domain.model.MilkOption
import com.example.brewkery.domain.model.SizeOption
import com.example.brewkery.domain.model.SugarOption

data class ItemDetailUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val item: MenuItem? = null,
    val size: SizeOption? = null,
    val sugar: SugarOption? = null,
    val milk: MilkOption? = null,
    val quantity: Int = 1,
    val isFavorite: Boolean = false
) {
    // Single source of truth for pricing: the same CartItem the cart will use
    val cartItem: CartItem?
        get() = if (item != null && size != null && sugar != null && milk != null) {
            CartItem(item, size, sugar, milk, quantity)
        } else null

    val unitPrice: Double get() = cartItem?.unitPrice ?: 0.0
    val total: Double get() = cartItem?.lineTotal ?: 0.0
}