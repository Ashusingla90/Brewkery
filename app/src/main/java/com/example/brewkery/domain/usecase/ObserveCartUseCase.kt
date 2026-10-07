package com.example.brewkery.domain.usecase


import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.model.CartTotals
import com.example.brewkery.domain.model.StoreInfo
import com.example.brewkery.domain.repository.CartRepository
import com.example.brewkery.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

data class CartSummary(
    val items: List<CartItem>,
    val store: StoreInfo?,
    val totals: CartTotals
)

class ObserveCartUseCase @Inject constructor(
    private val cartRepository: CartRepository,
    private val menuRepository: MenuRepository,
    private val calculateTotals: CalculateCartTotalUseCase
) {
    operator fun invoke(): Flow<CartSummary> =
        combine(cartRepository.items, menuRepository.store) { items, store ->
            CartSummary(
                items = items,
                store = store,
                totals = if (store != null) calculateTotals(items, store)
                else CartTotals(0.0, 0.0, 0.0, 0.0)
            )
        }
}