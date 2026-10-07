package com.example.brewkery.data.repository

import com.example.brewkery.data.local.ActiveOrderEntity
import com.example.brewkery.data.local.OrderDao
import com.example.brewkery.domain.model.Order
import com.example.brewkery.domain.repository.OrderRepository
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OrderRepositoryImpl @Inject constructor(
    private val dao: OrderDao
) : OrderRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val adapter = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
        .adapter(Order::class.java)

    private val _activeOrder = MutableStateFlow<Order?>(null)
    override val activeOrder: StateFlow<Order?> = _activeOrder.asStateFlow()

    init {
        scope.launch {
            val saved = dao.get()?.let { runCatching { adapter.fromJson(it.json) }.getOrNull() }
            _activeOrder.update { current -> current ?: saved }
        }
    }

    override fun setActiveOrder(order: Order) {
        _activeOrder.value = order
        scope.launch { dao.save(ActiveOrderEntity(json = adapter.toJson(order))) }
    }
}