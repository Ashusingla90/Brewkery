package com.example.brewkery.presentation.detail


import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.domain.model.MilkOption
import com.example.brewkery.domain.model.SizeOption
import com.example.brewkery.domain.model.SugarOption
import com.example.brewkery.domain.usecase.AddToCartUseCase
import com.example.brewkery.domain.usecase.GetItemDetailUseCase
import com.example.brewkery.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ItemDetailViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getItemDetail: GetItemDetailUseCase,
    private val addToCart: AddToCartUseCase
) : ViewModel() {

    private val itemId: Int = checkNotNull(savedStateHandle["itemId"])

    private val _state = MutableStateFlow(ItemDetailUiState())
    val state: StateFlow<ItemDetailUiState> = _state.asStateFlow()

    init { load() }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getItemDetail(itemId).fold(
                onSuccess = { item ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            item = item,
                            // Sensible defaults: first option in each group
                            size = item.sizes.firstOrNull(),
                            sugar = item.sugarLevels.firstOrNull(),
                            milk = item.milkOptions.firstOrNull()
                        )
                    }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
            )
        }
    }

    fun onSizeSelected(size: SizeOption) = _state.update { it.copy(size = size) }
    fun onSugarSelected(sugar: SugarOption) = _state.update { it.copy(sugar = sugar) }
    fun onMilkSelected(milk: MilkOption) = _state.update { it.copy(milk = milk) }

    fun onQuantityChange(delta: Int) =
        _state.update { it.copy(quantity = (it.quantity + delta).coerceIn(1, MAX_QUANTITY)) }

    fun toggleFavorite() = _state.update { it.copy(isFavorite = !it.isFavorite) }

    fun addToCart() {
        _state.value.cartItem?.let(addToCart::invoke)
    }

    private companion object {
        const val MAX_QUANTITY = 20
    }
}