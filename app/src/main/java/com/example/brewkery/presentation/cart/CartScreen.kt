package com.example.brewkery.presentation.cart

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.usecase.CartSummary
import com.example.brewkery.presentation.common.CircleIconButton
import com.example.brewkery.presentation.common.formatPrice
import com.example.brewkery.ui.theme.*

private val ClearRed = Color(0xFFD7263D)

@Composable
fun CartScreen(
    onBack: () -> Unit,
    onOrderPlaced: () -> Unit,
    viewModel: CartViewModel = hiltViewModel()
) {
    val summary by viewModel.state.collectAsStateWithLifecycle()
    CartContent(
        summary = summary,
        onBack = onBack,
        onClear = viewModel::clearCart,
        onQuantityChange = viewModel::onQuantityChange,
        onPlaceOrder = { viewModel.placeOrder(onOrderPlaced) }
    )
}

@Composable
private fun CartContent(
    summary: CartSummary?,
    onBack: () -> Unit,
    onClear: () -> Unit,
    onQuantityChange: (String, Int) -> Unit,
    onPlaceOrder: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(BrewCream)
            .statusBarsPadding()
    ) {
        val hasItems = summary != null && summary.items.isNotEmpty()
        TopBar(onBack = onBack, showClear = hasItems, onClear = onClear)

        when {
            summary == null -> Box(Modifier.weight(1f).fillMaxWidth(), Alignment.Center) {
                CircularProgressIndicator(color = BrewAccent)
            }

            summary.items.isEmpty() -> EmptyState(onBrowse = onBack, modifier = Modifier.weight(1f))

            else -> {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(summary.items, key = { it.lineId }) { line ->
                        CartLineCard(line, onQuantityChange)
                    }
                }

                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    TotalsCard(summary)
                    PlaceOrderButton(
                        total = summary.totals.total,
                        symbol = summary.store?.currencySymbol ?: "$",
                        enabled = summary.store != null,
                        onClick = onPlaceOrder
                    )
                }
            }
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit, showClear: Boolean, onClear: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CircleIconButton(onClick = onBack) {
            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = BrewInk)
        }
        Text(
            "YOUR CART",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = BrewInk,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        // Same width as the back button so the title stays centered
        Box(Modifier.width(72.dp), contentAlignment = Alignment.CenterEnd) {
            if (showClear) {
                Text(
                    "Clear Cart",
                    color = ClearRed,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable(onClick = onClear)
                )
            }
        }
    }
}

@Composable
private fun CartLineCard(line: CartItem, onQuantityChange: (String, Int) -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White)
            .border(1.dp, BrewBorder, shape)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(4.dp)) {
            Text(line.item.name, color = BrewInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            Text("${line.size.label} • ${line.milk.name}", color = BrewMuted, fontSize = 13.sp)
            Text(
                line.lineTotal.formatPrice(),
                color = BrewAccent,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
        Spacer(Modifier.width(12.dp))
        QuantityStepper(
            quantity = line.quantity,
            onChange = { delta -> onQuantityChange(line.lineId, line.quantity + delta) }
        )
    }
}

@Composable
private fun QuantityStepper(quantity: Int, onChange: (Int) -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        modifier = Modifier
            .clip(shape)
            .background(BrewBannerBg)
            .border(1.dp, BrewBorder, shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "−",
            color = BrewInk,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { onChange(-1) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
        Text(quantity.toString(), color = BrewInk, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        Text(
            "+",
            color = BrewInk,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { onChange(1) }
                .padding(horizontal = 12.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun TotalsCard(summary: CartSummary) {
    val symbol = summary.store?.currencySymbol ?: "$"
    val taxRate = summary.store?.taxRatePercent ?: 0.0
    val totals = summary.totals
    val shape = RoundedCornerShape(16.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White)
            .border(1.dp, BrewBorder, shape)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TotalsRow("Subtotal", totals.subtotal.formatPrice(symbol))
        TotalsRow("Delivery Fee", totals.deliveryFee.formatPrice(symbol))
        TotalsRow("Est. Tax ($taxRate%)", totals.tax.formatPrice(symbol))
        DashedDivider()
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Total Payable", color = BrewInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
            Text(
                totals.total.formatPrice(symbol),
                color = BrewAccent,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace
            )
        }
    }
}

@Composable
private fun TotalsRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = BrewMuted, fontSize = 15.sp)
        Text(value, color = BrewInk, fontSize = 15.sp, fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace)
    }
}

@Composable
private fun DashedDivider() {
    Canvas(Modifier.fillMaxWidth().height(1.dp)) {
        drawLine(
            color = BrewBorder,
            start = Offset(0f, 0f),
            end = Offset(size.width, 0f),
            strokeWidth = 2f,
            pathEffect = PathEffect.dashPathEffect(floatArrayOf(10f, 10f))
        )
    }
}

@Composable
private fun PlaceOrderButton(total: Double, symbol: String, enabled: Boolean, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(
                Brush.horizontalGradient(
                    if (enabled) listOf(BrewAccent, BrewAccentDark)
                    else listOf(BrewMuted, BrewMuted)
                )
            )
            .clickable(enabled = enabled, onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Lock, contentDescription = null, tint = Color.White, modifier = Modifier.size(16.dp))
            Spacer(Modifier.width(8.dp))
            Text(
                "Place Order Now  •  ${total.formatPrice(symbol)}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun EmptyState(onBrowse: () -> Unit, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("🛍️", fontSize = 48.sp)
        Spacer(Modifier.height(12.dp))
        Text("Your cart is empty", color = BrewInk, fontSize = 18.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("Add something delicious from the menu.", color = BrewMuted, fontSize = 14.sp)
        Spacer(Modifier.height(20.dp))
        Button(
            onClick = onBrowse,
            colors = ButtonDefaults.buttonColors(containerColor = BrewAccent)
        ) { Text("Browse Menu", color = Color.Black) }
    }
}