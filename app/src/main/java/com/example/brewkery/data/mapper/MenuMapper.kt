package com.example.brewkery.data.mapper

import com.example.brewkery.data.remote.*
import com.example.brewkery.domain.model.*

fun MenuResponseDto.toDomain() = Menu(
    store = meta.toDomain(),
    categories = categories.map { it.toDomain() },
    items = items.map { it.toDomain() }
)

fun MetaDto.toDomain() = StoreInfo(
    name = app,
    tagline = tagline,
    currencySymbol = currencySymbol,
    deliveryFee = deliveryFee,
    taxRatePercent = taxRatePercent,
    estimatedDeliveryTime = estimatedDeliveryTime
)

fun CategoryDto.toDomain() = Category(id = id, name = name, icon = icon)

fun ItemDto.toDomain() = MenuItem(
    id = id,
    categoryId = categoryId,
    name = name,
    tagline = tagline,
    description = description,
    basePrice = basePrice,
    rating = rating,
    reviewCount = reviewCount,
    prepTime = prepTime,
    calories = calories,
    imageUrl = imageUrl,
    badge = badge,
    ingredients = ingredients,
    sizes = customizations.sizes.map { SizeOption(it.id, it.label, it.extraPrice) },
    sugarLevels = customizations.sugarLevels.map { it.toSugarOption() },
    milkOptions = customizations.milkOptions.map { MilkOption(it.id, it.name, it.extraPrice) }
)

private val SURCHARGE_REGEX = Regex("""\(\+(\d+(?:\.\d+)?)\)""")

fun String.toSugarOption(): SugarOption {
    val surcharge = SURCHARGE_REGEX.find(this)?.groupValues?.get(1)?.toDoubleOrNull() ?: 0.0
    return SugarOption(label = this, extraPrice = surcharge)
}