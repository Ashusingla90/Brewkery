package com.example.brewkery.presentation.order


import androidx.lifecycle.ViewModel
import com.example.brewkery.domain.model.Order
import com.example.brewkery.domain.repository.OrderRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class OrderStatusViewModel @Inject constructor(
    orderRepository: OrderRepository
) : ViewModel() {
    // Already a StateFlow, so no extra mapping is needed
    val order: StateFlow<Order?> = orderRepository.activeOrder
}