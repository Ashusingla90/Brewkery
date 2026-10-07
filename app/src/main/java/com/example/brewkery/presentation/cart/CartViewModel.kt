package com.example.brewkery.presentation.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brewkery.domain.repository.CartRepository
import com.example.brewkery.domain.repository.MenuRepository
import com.example.brewkery.domain.usecase.CartSummary
import com.example.brewkery.domain.usecase.GetMenuUseCase
import com.example.brewkery.domain.usecase.ObserveCartUseCase
import com.example.brewkery.domain.usecase.PlaceOrderUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CartViewModel @Inject constructor(
    observeCart: ObserveCartUseCase,
    menuRepository: MenuRepository,
    getMenu: GetMenuUseCase,
    private val cartRepository: CartRepository,
    private val placeOrder: PlaceOrderUseCase
) : ViewModel() {

    // null only for the first frame, before the first emission
    val state: StateFlow<CartSummary?> = observeCart()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    init {
        // Totals need delivery fee and tax rate. If the menu was never loaded, load it now.
        if (menuRepository.store.value == null) {
            viewModelScope.launch { getMenu() }
        }
    }

    fun onQuantityChange(lineId: String, newQuantity: Int) =
        cartRepository.updateQuantity(lineId, newQuantity)   // <= 0 removes the line

    fun clearCart() = cartRepository.clear()

    fun placeOrder(onSuccess: () -> Unit) {
        val store = state.value?.store ?: return
        placeOrder(store).onSuccess { onSuccess() }
    }
}