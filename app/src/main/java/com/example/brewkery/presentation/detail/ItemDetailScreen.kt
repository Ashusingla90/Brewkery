package com.example.brewkery.presentation.detail


import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
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
import com.example.brewkery.domain.model.MenuItem
import com.example.brewkery.domain.model.MilkOption
import com.example.brewkery.domain.model.SizeOption
import com.example.brewkery.domain.model.SugarOption
import com.example.brewkery.presentation.common.CircleIconButton
import com.example.brewkery.presentation.common.ErrorState
import com.example.brewkery.presentation.common.formatPrice
import com.example.brewkery.ui.theme.*

private val SelectedBg = Color(0xFFF8E7DF)
private val HeartRed = Color(0xFFE5445B)
private val BadgeOrange = Color(0xFFF5A623)
private val GradientEnd = Color(0xFF8E3A25)

@Composable
fun ItemDetailScreen(
    onBack: () -> Unit,
    onAddedToCart: () -> Unit,
    viewModel: ItemDetailViewModel = hiltViewModel()
) {
    val state by viewModel.state.collectAsStateWithLifecycle()
    ItemDetailContent(
        state = state,
        onBack = onBack,
        onToggleFavorite = viewModel::toggleFavorite,
        onSizeSelected = viewModel::onSizeSelected,
        onMilkSelected = viewModel::onMilkSelected,
        onSugarSelected = viewModel::onSugarSelected,
        onQuantityChange = viewModel::onQuantityChange,
        onAddToCart = {
            viewModel.addToCart()
            onAddedToCart()
        },
        onRetry = viewModel::load
    )
}

@Composable
private fun ItemDetailContent(
    state: ItemDetailUiState,
    onBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onSizeSelected: (SizeOption) -> Unit,
    onMilkSelected: (MilkOption) -> Unit,
    onSugarSelected: (SugarOption) -> Unit,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit,
    onRetry: () -> Unit
) {
    Column(
        Modifier
            .fillMaxSize()
            .background(BrewCream)
            .statusBarsPadding()
    ) {
        TopBar(onBack, state.isFavorite, onToggleFavorite)

        val item = state.item
        when {
            state.isLoading -> Box(Modifier.weight(1f).fillMaxWidth(), Alignment.Center) {
                CircularProgressIndicator(color = BrewAccent)
            }

            state.error != null -> Box(Modifier.weight(1f).fillMaxWidth(), Alignment.Center) {
                ErrorState(message = state.error, onRetry = onRetry)
            }

            item != null -> {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Hero(item)
                    TitleAndPrice(item.name, state.unitPrice)
                    Text(item.description, color = BrewMuted, fontSize = 14.sp, lineHeight = 21.sp)
                    Ingredients(item.ingredients)

                    SectionCard("Size Selection") {
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            item.sizes.forEach { size ->
                                SizeBox(
                                    size = size,
                                    selected = size.id == state.size?.id,
                                    onClick = { onSizeSelected(size) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }

                    SectionCard("Milk Options / Spreads") {
                        item.milkOptions.forEach { milk ->
                            MilkRow(
                                milk = milk,
                                selected = milk.id == state.milk?.id,
                                onClick = { onMilkSelected(milk) }
                            )
                        }
                    }

                    SectionCard("Sugar Levels / Serving") {
                        SugarChips(item.sugarLevels, state.sugar, onSugarSelected)
                    }

                    Spacer(Modifier.height(4.dp))
                }

                BottomBar(
                    quantity = state.quantity,
                    total = state.total,
                    onQuantityChange = onQuantityChange,
                    onAddToCart = onAddToCart
                )
            }
        }
    }
}

@Composable
private fun TopBar(onBack: () -> Unit, isFavorite: Boolean, onToggleFavorite: () -> Unit) {
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
            "ITEM CUSTOMIZER",
            modifier = Modifier.weight(1f),
            textAlign = TextAlign.Center,
            color = BrewInk,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold
        )
        CircleIconButton(onClick = onToggleFavorite) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = "Favorite",
                tint = HeartRed
            )
        }
    }
}



@Composable
private fun Hero(item: MenuItem) {
    Box(
        Modifier
            .fillMaxWidth()
            .height(200.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(BrewBorder)
    ) {
        AsyncImage(
            model = item.imageUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        item.badge?.let { badge ->
            Text(
                text = badge,
                color = BadgeOrange,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(12.dp)
                    .clip(RoundedCornerShape(50))
                    .background(BrewInk.copy(alpha = 0.85f))
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            )
        }
    }
}

@Composable
private fun TitleAndPrice(name: String, price: Double) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            name,
            modifier = Modifier.weight(1f),
            color = BrewInk,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )
        Spacer(Modifier.width(12.dp))
        Text(
            price.formatPrice(),
            color = BrewAccent,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            fontFamily = FontFamily.Monospace
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun Ingredients(ingredients: List<String>) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            "KEY INGREDIENTS",
            color = BrewMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ingredients.forEach { ingredient ->
                Text(
                    ingredient,
                    color = BrewMuted,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .clip(RoundedCornerShape(50))
                        .border(1.dp, BrewBorder, RoundedCornerShape(50))
                        .padding(horizontal = 12.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun SectionCard(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(1.dp, BrewBorder, RoundedCornerShape(16.dp))
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(title, color = BrewInk, fontSize = 15.sp, fontWeight = FontWeight.Bold)
        content()
    }
}

@Composable
private fun SizeBox(
    size: SizeOption,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val shape = RoundedCornerShape(10.dp)
    Column(
        modifier = modifier
            .clip(shape)
            .background(if (selected) SelectedBg else Color.White)
            .border(1.dp, if (selected) BrewAccent else BrewBorder, shape)
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            size.label,
            color = if (selected) BrewAccent else BrewInk,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Center
        )
        Spacer(Modifier.height(2.dp))
        Text(
            "+${size.extraPrice.formatPrice()}",
            color = if (selected) BrewAccent else BrewMuted,
            fontSize = 12.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@Composable
private fun MilkRow(milk: MilkOption, selected: Boolean, onClick: () -> Unit) {
    val shape = RoundedCornerShape(10.dp)
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(if (selected) SelectedBg else Color.White)
            .border(1.dp, if (selected) BrewAccent else BrewBorder, shape)
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            milk.name,
            modifier = Modifier.weight(1f),
            color = if (selected) BrewAccent else BrewMuted,
            fontSize = 15.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
        )
        Text(
            "+${milk.extraPrice.formatPrice()}",
            color = if (selected) BrewAccent else BrewMuted,
            fontSize = 13.sp,
            fontFamily = FontFamily.Monospace
        )
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SugarChips(
    options: List<SugarOption>,
    selected: SugarOption?,
    onSelected: (SugarOption) -> Unit
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            val isSelected = option.label == selected?.label
            Text(
                option.label,
                color = if (isSelected) Color.White else BrewInk,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .background(if (isSelected) BrewInk else Color.White)
                    .border(1.dp, if (isSelected) BrewInk else BrewBorder, RoundedCornerShape(10.dp))
                    .clickable { onSelected(option) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            )
        }
    }
}

@Composable
private fun BottomBar(
    quantity: Int,
    total: Double,
    onQuantityChange: (Int) -> Unit,
    onAddToCart: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(BrewCream)
            .navigationBarsPadding()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        QuantityStepper(quantity, onQuantityChange)
        Box(
            modifier = Modifier
                .weight(1f)
                .height(52.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(Brush.horizontalGradient(listOf(BrewAccent, GradientEnd)))
                .clickable(onClick = onAddToCart),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "Add to Cart  •  ${total.formatPrice()}",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun QuantityStepper(quantity: Int, onChange: (Int) -> Unit) {
    val shape = RoundedCornerShape(14.dp)
    Row(
        modifier = Modifier
            .height(52.dp)
            .clip(shape)
            .background(Color.White)
            .border(1.dp, BrewBorder, shape),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "−",
            color = if (quantity > 1) BrewInk else BrewMuted,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable(enabled = quantity > 1) { onChange(-1) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
        Text(quantity.toString(), color = BrewInk, fontSize = 16.sp, fontWeight = FontWeight.Bold)
        Text(
            "+",
            color = BrewInk,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .clickable { onChange(1) }
                .padding(horizontal = 16.dp, vertical = 12.dp)
        )
    }
}