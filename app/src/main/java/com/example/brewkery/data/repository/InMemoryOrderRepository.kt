package com.example.brewkery.data.repository


import com.example.brewkery.domain.model.Order
import com.example.brewkery.domain.repository.OrderRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class InMemoryOrderRepository @Inject constructor() : OrderRepository {
    private val _activeOrder = MutableStateFlow<Order?>(null)
    override val activeOrder: StateFlow<Order?> = _activeOrder.asStateFlow()

    override fun setActiveOrder(order: Order) {
        _activeOrder.value = order
    }
}