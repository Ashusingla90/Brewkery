package com.example.brewkery.domain.usecase


import com.example.brewkery.domain.model.Order
import com.example.brewkery.domain.model.StoreInfo
import com.example.brewkery.domain.repository.CartRepository
import com.example.brewkery.domain.repository.OrderRepository
import javax.inject.Inject

class PlaceOrderUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val orderRepository: OrderRepository,
    private val calculateTotals: CalculateCartTotalUseCase
) {
    operator fun invoke(store: StoreInfo): Result<Order> {
        val items = cartRepository.items.value
        if (items.isEmpty()) return Result.failure(IllegalStateException("Cart is empty"))

        val order = Order(
            ticketId = "BK-${(10000..99999).random()}",
            items = items,
            totals = calculateTotals(items, store),
            estimatedDeliveryTime = store.estimatedDeliveryTime
        )
        orderRepository.setActiveOrder(order)
        cartRepository.clear()          // the order keeps its own copy of the items
        return Result.success(order)
    }
}