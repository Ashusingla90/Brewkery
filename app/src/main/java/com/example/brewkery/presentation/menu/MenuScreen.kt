package com.example.brewkery.presentation.menu

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.example.brewkery.domain.model.Category
import com.example.brewkery.domain.model.MenuItem
import com.example.brewkery.domain.model.Order
import com.example.brewkery.domain.model.StoreInfo
import com.example.brewkery.presentation.common.ErrorState
import com.example.brewkery.presentation.common.formatPrice
import com.example.brewkery.ui.theme.*

@Composable
fun MenuScreen(
    onItemClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onOrderClick: () -> Unit,
    viewModel: MenuViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    MenuContent(
        state = state,
        onQueryChange = viewModel::onQueryChange,
        onCategorySelected = viewModel::onCategorySelected,
        onRetry = viewModel::load,
        onItemClick = onItemClick,
        onCartClick = onCartClick,
        onOrderClick = onOrderClick
    )
}

@Composable
private fun MenuContent(
    state: MenuUiState,
    onQueryChange: (String) -> Unit,
    onCategorySelected: (String?) -> Unit,
    onRetry: () -> Unit,
    onItemClick: (Int) -> Unit,
    onCartClick: () -> Unit,
    onOrderClick: () -> Unit
) {
    Box(
        Modifier
            .fillMaxSize()
            .background(BrewCream)
            .statusBarsPadding()
    ) {
        when {
            state.isLoading -> CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center),
                color = BrewAccent
            )

            state.error != null -> ErrorState(
                message = state.error,
                onRetry = onRetry,
                modifier = Modifier.align(Alignment.Center)
            )

            else -> LazyColumn(
                contentPadding = PaddingValues(
//                    horizontal = 16.dp, vertical = 12.dp
                    start = 16.dp, end = 16.dp, top = 12.dp, bottom = 100.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item { Header(cartCount = state.cartCount, onCartClick = onCartClick) }
                state.store?.let { store -> item { StoreBanner(store) } }
                state.activeOrder?.let { order ->
                    item { ActiveOrderBanner(order, onOrderClick) }
                }
                item { SearchBar(state.query, onQueryChange) }
                item {
                    CategoryRow(state.categories, state.selectedCategoryId, onCategorySelected)
                }
                items(state.visibleItems, key = { it.id }) { menuItem ->
                    MenuItemCard(
                        item = menuItem,
                        currencySymbol = state.store?.currencySymbol ?: "$",
                        onClick = { onItemClick(menuItem.id) }
                    )
                }
                if (state.visibleItems.isEmpty()) {
                    item {
                        Text(
                            text = "No items match your search.",
                            color = BrewMuted,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 32.dp)
                        )
                    }
                }
            }
        }

        if (!state.isLoading && state.error == null && state.cartCount > 0) {
            CartBar(
                cartCount = state.cartCount,
                totalText = state.cartTotal.formatPrice(state.store?.currencySymbol ?: "$"),
                onClick = onCartClick,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .navigationBarsPadding()
                    .padding(16.dp)
            )
        }
    }
}



@Composable
private fun Header(cartCount: Int, onCartClick: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .clip(CircleShape)
                .background(BrewInk),
            contentAlignment = Alignment.Center
        ) {
            Text("BK", color = Color.White, fontWeight = FontWeight.Bold)
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "Fresh Roast & Bakes",
                color = BrewMuted,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Text(
                "Brewkery Artisans",
                color = BrewInk,
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Box(modifier = Modifier.size(44.dp)) {

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(1.dp, BrewBorder, CircleShape)
                    .clickable(onClick = onCartClick),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = "Cart", tint = BrewInk)
            }
            if (cartCount > 0) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .offset(x = 6.dp, y = (-6).dp)
                        .size(20.dp)
                        .clip(CircleShape)
                        .background(BrewAccent),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        cartCount.toString(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}


@Composable
private fun StoreBanner(store: StoreInfo) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(BrewBannerBg)
            .border(1.dp, BrewBorder, RoundedCornerShape(16.dp))
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(44.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) { Text("🛵", fontSize = 22.sp) }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("STORE INFO ●", color = BrewAccent, fontSize = 11.sp, fontWeight = FontWeight.Bold)
            Text(
                "Delivery in ${store.estimatedDeliveryTime}",
                color = BrewInk, fontSize = 16.sp, fontWeight = FontWeight.SemiBold
            )
            Text(
                "${store.deliveryFee.formatPrice(store.currencySymbol)} flat fee",
                color = BrewMuted, fontSize = 13.sp
            )
        }
        Text(
            "Open",
            color = BrewInk, fontSize = 13.sp, fontWeight = FontWeight.SemiBold,
            modifier = Modifier
                .clip(RoundedCornerShape(10.dp))
                .background(Color.White)
                .padding(horizontal = 14.dp, vertical = 8.dp)
        )
    }
}

@Composable
private fun ActiveOrderBanner(order: Order, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(BrewInk)
            .clickable(onClick = onClick)
            .padding(14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text("☕", fontSize = 24.sp)
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text(
                "ORDER #${order.ticketId}",
                color = Color.White, fontSize = 13.sp, fontWeight = FontWeight.Bold
            )
            Text(
                "${order.status.name} • ${order.estimatedDeliveryTime}",
                color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp
            )
        }
        Text("Track ›", color = BrewAccent, fontSize = 13.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SearchBar(query: String, onQueryChange: (String) -> Unit) {
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        shape = RoundedCornerShape(16.dp),
        placeholder = { Text("Search roast, cold brew, pastry...", color = BrewMuted) },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = BrewMuted) },
        colors = OutlinedTextFieldDefaults.colors(
            focusedContainerColor = Color.White,
            unfocusedContainerColor = Color.White,
            focusedBorderColor = BrewAccent,
            unfocusedBorderColor = BrewBorder
        )
    )
}

@Composable
private fun CategoryRow(
    categories: List<Category>,
    selectedId: String?,
    onSelected: (String?) -> Unit
) {
    LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
        item { CategoryChip("All Items", selectedId == null) { onSelected(null) } }
        items(categories, key = { it.id }) { category ->
            CategoryChip("${category.icon} ${category.name}", selectedId == category.id) {
                onSelected(category.id)
            }
        }
    }
}

@Composable
private fun CategoryChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Text(
        text = label,
        color = if (selected) Color.White else BrewInk,
        fontSize = 14.sp,
        fontWeight = FontWeight.SemiBold,
        modifier = Modifier
            .clip(CircleShape)
            .background(if (selected) BrewInk else Color.White)
            .border(1.dp, if (selected) BrewInk else BrewBorder, CircleShape)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 10.dp)
    )
}

@Composable
private fun MenuItemCard(item: MenuItem, currencySymbol: String, onClick: () -> Unit) {
    val shape = RoundedCornerShape(16.dp)
    Card(
        shape = shape,
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, BrewBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .clickable(onClick = onClick)
    ) {
        Row(Modifier.padding(12.dp)) {
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(BrewBorder)
            )
            Spacer(Modifier.width(12.dp))
            Column(Modifier.weight(1f)) {
                item.badge?.let { badge ->
                    Text(
                        text = badge,
                        color = BrewBadgeText,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(BrewBadgeBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                    Spacer(Modifier.height(4.dp))
                }
                Text(item.name, color = BrewInk, fontSize = 16.sp, fontWeight = FontWeight.SemiBold)
                Spacer(Modifier.height(4.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        Icons.Default.Star, contentDescription = null,
                        tint = Color(0xFFF5B400), modifier = Modifier.size(14.dp)
                    )
                    Spacer(Modifier.width(2.dp))
                    Text("${item.rating} (${item.reviewCount})", color = BrewMuted, fontSize = 13.sp)
                }
                Spacer(Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        item.basePrice.formatPrice(currencySymbol),
                        color = BrewAccent, fontSize = 16.sp,
                        fontWeight = FontWeight.Bold, fontFamily = FontFamily.Monospace
                    )
                    Text(
                        "+ Customize",
                        color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.SemiBold,
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(BrewAccent)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }
    }
}


@Composable
private fun CartBar(
    cartCount: Int,
    totalText: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(BrewInk)
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(BrewAccent),
            contentAlignment = Alignment.Center
        ) {
            Text(
                cartCount.toString(),
                color = Color.White,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.width(12.dp))
        Column(Modifier.weight(1f)) {
            Text("View Your Cart", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp)
            Text(totalText, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        Text(
            "Proceed to Checkout →",
            color = Color(0xFFF5B400),
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )
    }
}