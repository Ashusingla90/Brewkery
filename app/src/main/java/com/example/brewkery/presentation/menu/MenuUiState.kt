package com.example.brewkery.presentation.menu

import com.example.brewkery.domain.model.Category
import com.example.brewkery.domain.model.MenuItem
import com.example.brewkery.domain.model.Order
import com.example.brewkery.domain.model.StoreInfo

data class MenuUiState(
    val isLoading: Boolean = true,
    val error: String? = null,
    val store: StoreInfo? = null,
    val categories: List<Category> = emptyList(),
    val items: List<MenuItem> = emptyList(),
    val selectedCategoryId: String? = null,   // null = "All Items"
    val query: String = "",
    val cartCount: Int = 0,
    val activeOrder: Order? = null,
    val cartTotal: Double = 0.0
) {
    val visibleItems: List<MenuItem>
        get() = items.filter { item ->
            (selectedCategoryId == null || item.categoryId == selectedCategoryId) &&
                    (query.isBlank() ||
                            item.name.contains(query, ignoreCase = true) ||
                            item.tagline.contains(query, ignoreCase = true))
        }
}