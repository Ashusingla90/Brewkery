package com.example.brewkery.presentation.common

import java.util.Locale

fun Double.formatPrice(symbol: String = "$"): String =
    symbol + String.format(Locale.US, "%.2f", this)