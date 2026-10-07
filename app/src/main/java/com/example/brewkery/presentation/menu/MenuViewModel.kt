package com.example.brewkery.presentation.menu


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.model.StoreInfo
import com.example.brewkery.domain.repository.CartRepository
import com.example.brewkery.domain.repository.OrderRepository
import com.example.brewkery.domain.usecase.CalculateCartTotalUseCase
import com.example.brewkery.domain.usecase.GetMenuUseCase
import com.example.brewkery.presentation.common.toUserMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val getMenuUseCase: GetMenuUseCase,
    cartRepository: CartRepository,
    orderRepository: OrderRepository,
    private val calculateCartTotal: CalculateCartTotalUseCase
) : ViewModel() {
    private var cartItems: List<CartItem> = emptyList()
    private val _state = MutableStateFlow(MenuUiState())
    val state: StateFlow<MenuUiState> = _state.asStateFlow()

    init {
        load()
        viewModelScope.launch {
            cartRepository.items.collect { items ->
                cartItems = items
                _state.update {
                    it.copy(
                        cartCount = items.sumOf { line -> line.quantity },
                        cartTotal = totalFor(items, it.store)
                    ) }
            }
        }

        viewModelScope.launch {
            orderRepository.activeOrder.collect { order ->
                _state.update { it.copy(activeOrder = order) }
            }
        }
    }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, error = null) }
            getMenuUseCase().fold(
                onSuccess = { menu ->
                    _state.update {
                        it.copy(
                            isLoading = false,
                            store = menu.store,
                            categories = menu.categories,
                            items = menu.items,
                            cartTotal = totalFor(cartItems, menu.store)
                        )
                    }
                },
                onFailure = { e ->
                    _state.update { it.copy(isLoading = false, error = e.toUserMessage()) }
                }
            )
        }
    }

    fun onQueryChange(query: String) = _state.update { it.copy(query = query) }

    fun onCategorySelected(categoryId: String?) =
        _state.update { it.copy(selectedCategoryId = categoryId) }


    private fun totalFor(items: List<CartItem>, store: StoreInfo?): Double =
        store?.let { calculateCartTotal(items, it).total } ?: 0.0
}