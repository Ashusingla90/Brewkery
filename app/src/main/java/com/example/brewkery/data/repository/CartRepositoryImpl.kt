package com.example.brewkery.data.repository

import com.example.brewkery.data.local.CartDao
import com.example.brewkery.data.local.CartLineEntity
import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.repository.CartRepository
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
class CartRepositoryImpl @Inject constructor(
    private val dao: CartDao
) : CartRepository {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private val dbDispatcher = Dispatchers.IO.limitedParallelism(1) // writes order me hon
    private val adapter = Moshi.Builder()
        .addLast(KotlinJsonAdapterFactory())
        .build()
        .adapter(CartItem::class.java)

    private val _items = MutableStateFlow<List<CartItem>>(emptyList())
    override val items: StateFlow<List<CartItem>> = _items.asStateFlow()

    init {
        scope.launch {
            val saved = dao.getAll().mapNotNull {
                runCatching { adapter.fromJson(it.json) }.getOrNull()
            }
            _items.update { current -> if (current.isEmpty()) saved else current }
        }
    }

    override fun add(cartItem: CartItem) {
        _items.update { list ->
            val existing = list.find { it.lineId == cartItem.lineId }
            if (existing == null) list + cartItem
            else list.map {
                if (it.lineId == cartItem.lineId)
                    it.copy(quantity = it.quantity + cartItem.quantity)
                else it
            }
        }
        persist()
    }

    override fun updateQuantity(lineId: String, quantity: Int) {
        if (quantity <= 0) return remove(lineId)
        _items.update { list ->
            list.map { if (it.lineId == lineId) it.copy(quantity = quantity) else it }
        }
        persist()
    }

    override fun remove(lineId: String) {
        _items.update { list -> list.filterNot { it.lineId == lineId } }
        persist()
    }

    override fun clear() {
        _items.value = emptyList()
        persist()
    }

    private fun persist() {
        val snapshot = _items.value
        scope.launch(dbDispatcher) {
            dao.replaceAll(
                snapshot.mapIndexed { index, item ->
                    CartLineEntity(item.lineId, index, adapter.toJson(item))
                }
            )
        }
    }
}