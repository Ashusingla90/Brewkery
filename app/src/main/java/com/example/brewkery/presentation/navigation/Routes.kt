package com.example.brewkery.presentation.navigation

object Routes {
    const val MENU = "menu"
    const val DETAIL = "detail/{itemId}"
    const val CART = "cart"
    const val ORDER = "order"
    fun detail(id: Int) = "detail/$id"
}