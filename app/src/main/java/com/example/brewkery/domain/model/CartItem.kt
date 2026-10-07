package com.example.brewkery.domain.model


import java.math.BigDecimal
import java.math.RoundingMode

fun Double.roundMoney(): Double =
    BigDecimal.valueOf(this).setScale(2, RoundingMode.HALF_UP).toDouble()

data class CartItem(
    val item: MenuItem,
    val size: SizeOption,
    val sugar: SugarOption,
    val milk: MilkOption,
    val quantity: Int
) {
    // Size IDs repeat across items, so identity must include everything chosen
    val lineId: String get() = "${item.id}|${size.id}|${sugar.label}|${milk.id}"

    val unitPrice: Double
        get() = (item.basePrice + size.extraPrice + sugar.extraPrice + milk.extraPrice).roundMoney()

    val lineTotal: Double get() = (unitPrice * quantity).roundMoney()
}

data class CartTotals(
    val subtotal: Double,
    val deliveryFee: Double,
    val tax: Double,
    val total: Double
)