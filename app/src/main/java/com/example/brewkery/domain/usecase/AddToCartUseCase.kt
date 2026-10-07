package com.example.brewkery.domain.usecase


import com.example.brewkery.domain.model.CartItem
import com.example.brewkery.domain.repository.CartRepository
import javax.inject.Inject

class AddToCartUseCase @Inject constructor(
    private val cartRepository: CartRepository
) {
    operator fun invoke(cartItem: CartItem) = cartRepository.add(cartItem)
}