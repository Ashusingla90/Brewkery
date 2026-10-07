package com.example.brewkery.domain.usecase


import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.model.CartTotals
import com.example.brewkery.domain.model.StoreInfo
import com.example.brewkery.domain.model.roundMoney
import javax.inject.Inject

class CalculateCartTotalUseCase @Inject constructor() {

    operator fun invoke(items: List<CartItem>, store: StoreInfo): CartTotals {
        if (items.isEmpty()) return CartTotals(0.0, 0.0, 0.0, 0.0)

        val subtotal = items.sumOf { it.lineTotal }.roundMoney()
        val tax = (subtotal * store.taxRatePercent / 100).roundMoney()
        val delivery = store.deliveryFee
        val total = (subtotal + tax + delivery).roundMoney()

        return CartTotals(subtotal, delivery, tax, total)
    }
}