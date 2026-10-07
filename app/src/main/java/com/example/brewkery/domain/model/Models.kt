package com.example.brewkery.domain.model


data class SugarOption(val label: String, val extraPrice: Double)
data class Menu(
//    val sugarLevels: List<SugarOption>,
    val store: StoreInfo,
    val categories: List<Category>,
    val items: List<MenuItem>
)

data class StoreInfo(
    val name: String,
    val tagline: String,
    val currencySymbol: String,
    val deliveryFee: Double,
    val taxRatePercent: Double,
    val estimatedDeliveryTime: String
)

data class Category(val id: String, val name: String, val icon: String)


data class SizeOption(val id: String, val label: String, val extraPrice: Double)

data class MilkOption(val id: String, val name: String, val extraPrice: Double)