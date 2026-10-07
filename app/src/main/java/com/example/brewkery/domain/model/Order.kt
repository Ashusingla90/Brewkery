package com.example.brewkery.domain.model


enum class OrderStatus { PREPARING }

data class Order(
    val ticketId: String,
    val items: List<CartItem>,
    val totals: CartTotals,
    val estimatedDeliveryTime: String,
    val status: OrderStatus = OrderStatus.PREPARING
)

