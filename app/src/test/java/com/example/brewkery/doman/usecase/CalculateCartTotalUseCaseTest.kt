package com.example.brewkery.doman.usecase

import com.example.brewkery.domain.usecase.CalculateCartTotalUseCase
import com.example.brewkery.data.mapper.toSugarOption
import com.example.brewkery.domain.model.*
import org.junit.Test
import org.junit.Assert.assertEquals

class CalculateCartTotalUseCaseTest {

    private val useCase = CalculateCartTotalUseCase()

    private val store = StoreInfo(
        name = "Brewkery", tagline = "", currencySymbol = "$",
        deliveryFee = 2.50, taxRatePercent = 8.0, estimatedDeliveryTime = "20 - 30 mins"
    )

    private fun cartItem(
        id: Int, base: Double,
        sizeExtra: Double = 0.0, sugarExtra: Double = 0.0, milkExtra: Double = 0.0,
        qty: Int = 1
    ) = CartItem(
        item = MenuItem(
            id = id, categoryId = "c", name = "Item $id", tagline = "", description = "",
            basePrice = base, rating = 4.5, reviewCount = 1, prepTime = "", calories = 0,
            imageUrl = "", badge = null, ingredients = emptyList(),
            sizes = emptyList(), sugarLevels = emptyList(), milkOptions = emptyList()
        ),
        size = SizeOption("sz", "Size", sizeExtra),
        sugar = SugarOption("Sugar", sugarExtra),
        milk = MilkOption("m", "Milk", milkExtra),
        quantity = qty
    )

    @Test
    fun `empty cart has zero totals and no delivery fee`() {
        val totals = useCase(emptyList(), store)
        assertEquals(0.0, totals.total, 0.001)
        assertEquals(0.0, totals.deliveryFee, 0.001)
    }

    @Test
    fun `totals combine customizations, quantity, tax and delivery`() {
        val items = listOf(
            // 4.85 + 0.65 (Grande) + 0.50 (almond) = 6.00 x 2 = 12.00
            cartItem(id = 1, base = 4.85, sizeExtra = 0.65, milkExtra = 0.50, qty = 2),
            // 3.90 x 1
            cartItem(id = 3, base = 3.90)
        )
        val totals = useCase(items, store)

        assertEquals(15.90, totals.subtotal, 0.001)
        assertEquals(1.27, totals.tax, 0.001)        // 8% of 15.90 = 1.272
        assertEquals(2.50, totals.deliveryFee, 0.001)
        assertEquals(19.67, totals.total, 0.001)
    }

    @org.junit.Test
    fun `sugar surcharge in label is parsed and charged`() {
        val sugar = "Light Wildflower Honey (+0.40)".toSugarOption()
        assertEquals(0.40, sugar.extraPrice, 0.001)

        val item = cartItem(id = 6, base = 5.60, sugarExtra = sugar.extraPrice)
        assertEquals(6.00, item.unitPrice, 0.001)
    }

    @Test
    fun `sugar label without surcharge is free`() {
        assertEquals(0.0, "Zero Added Sugar".toSugarOption().extraPrice, 0.001)
    }
}