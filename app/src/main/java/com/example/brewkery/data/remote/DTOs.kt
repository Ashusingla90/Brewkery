package com.example.brewkery.data.remote

import com.squareup.moshi.Json

data class MenuResponseDto(
    val meta: MetaDto,
    val categories: List<CategoryDto>,
    val items: List<ItemDto>
)

data class MetaDto(
    val app: String,
    val tagline: String,
    @Json(name = "currency_symbol") val currencySymbol: String,
    @Json(name = "delivery_fee") val deliveryFee: Double,
    @Json(name = "tax_rate_percent") val taxRatePercent: Double,
    @Json(name = "estimated_delivery_time") val estimatedDeliveryTime: String
)

data class CategoryDto(
    val id: String,
    val name: String,
    val icon: String
)

data class ItemDto(
    val id: Int,
    @Json(name = "category_id") val categoryId: String,
    val name: String,
    val tagline: String,
    val description: String,
    @Json(name = "base_price") val basePrice: Double,
    val rating: Double,
    @Json(name = "review_count") val reviewCount: Int,
    @Json(name = "prep_time") val prepTime: String,
    val calories: Int,
    @Json(name = "image_url") val imageUrl: String,
    val badge: String?,
    val ingredients: List<String>,
    val customizations: CustomizationsDto
)

data class CustomizationsDto(
    val sizes: List<SizeDto>,
    @Json(name = "sugar_levels") val sugarLevels: List<String>,
    @Json(name = "milk_options") val milkOptions: List<MilkOptionDto>
)

data class SizeDto(
    val id: String,
    val label: String,
    @Json(name = "extra_price") val extraPrice: Double
)

data class MilkOptionDto(
    val id: String,
    val name: String,
    @Json(name = "extra_price") val extraPrice: Double
)