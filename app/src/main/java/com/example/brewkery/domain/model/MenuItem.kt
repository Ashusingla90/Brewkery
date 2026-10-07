package com.example.brewkery.domain.model

data class MenuItem(
    val id: Int,
    val categoryId: String,
    val name: String,
    val tagline: String,
    val description: String,
    val basePrice: Double,
    val rating: Double,
    val reviewCount: Int,
    val prepTime: String,
    val calories: Int,
    val imageUrl: String,
    val badge: String?,
    val ingredients: List<String>,
    val sizes: List<SizeOption>,
    val sugarLevels: List<SugarOption>,
    val milkOptions: List<MilkOption>
)