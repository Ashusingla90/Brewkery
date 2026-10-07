package com.example.brewkery.presentation.order

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.brewkery.domain.model.Order
import com.example.brewkery.ui.theme.*

private val StatusGreen = Color(0xFF2E7D4F)

@Composable
fun OrderStatusScreen(
    onBackToMenu: () -> Unit,
    viewModel: OrderStatusViewModel = hiltViewModel()
) {
    val order by viewModel.order.collectAsStateWithLifecycle()
    OrderStatusContent(order = order, onBackToMenu = onBackToMenu)
}

@Composable
private fun OrderStatusContent(order: Order?, onBackToMenu: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BrewCream)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(16.dp)
    ) {
        if (order == null) {
            // Only reachable if the process was recreated; the order lives in memory
            Box(Modifier.weight(1f).fillMaxWidth(), Alignment.Center) {
                Text("No active order.", color = BrewMuted, fontSize = 16.sp)
            }
        } else {
            Spacer(Modifier.height(48.dp))
            Header()
            Spacer(Modifier.weight(1f))
            TicketCard(order)
            Spacer(Modifier.weight(1f))
        }
        BackToMenuButton(onBackToMenu)
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(112.dp)
                .clip(CircleShape)
                .background(BrewBannerBg)
                .border(2.dp, BrewAccent, CircleShape),
            contentAlignment = Alignment.Center
        ) { Text("☕", fontSize = 44.sp) }

        Spacer(Modifier.height(16.dp))
        Text(
            "ORDER DISPATCHED",
            color = BrewAccent,
            fontSize = 13.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        Spacer(Modifier.height(4.dp))
        Text("Brewing in Progress!", color = BrewInk, fontSize = 26.sp, fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text(
            "Your ticket was dispatched to our barista.",
            color = BrewMuted,
            fontSize = 15.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
private fun TicketCard(order: Order) {
    val shape = RoundedCornerShape(20.dp)
    val itemCount = order.items.sumOf { it.quantity }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(Color.White)
            .border(1.dp, BrewBorder, shape)
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text("ORDER TICKET", color = BrewMuted, fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                Text("#${order.ticketId}", color = BrewInk, fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }
            Text(
                order.status.name,
                color = BrewBadgeText,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(BrewBadgeBg)
                    .padding(horizontal = 14.dp, vertical = 8.dp)
            )
        }

        HorizontalDivider(Modifier.padding(vertical = 14.dp), color = BrewBorder)

        DetailRow("Estimated Wait:", order.estimatedDeliveryTime, valueColor = BrewAccent)
        Spacer(Modifier.height(10.dp))
        DetailRow(
            "Items Ordered:",
            "$itemCount ${if (itemCount == 1) "Item" else "Items"}",
            valueColor = BrewInk
        )

        HorizontalDivider(Modifier.padding(vertical = 14.dp), color = BrewBorder)

        Text("Status:", color = BrewMuted, fontSize = 12.sp)
        Spacer(Modifier.height(2.dp))
        Text(
            "Barista accepted your order!",
            color = StatusGreen,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
private fun DetailRow(label: String, value: String, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, color = BrewMuted, fontSize = 15.sp)
        Text(value, color = valueColor, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun BackToMenuButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(BrewInk)
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Text("Back to Menu", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
    }
}