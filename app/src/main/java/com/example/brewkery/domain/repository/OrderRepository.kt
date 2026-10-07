package com.example.brewkery.domain.repository


import com.example.brewkery.domain.model.Order
import kotlinx.coroutines.flow.StateFlow

interface OrderRepository {
    val activeOrder: StateFlow<Order?>
    fun setActiveOrder(order: Order)
}