package com.example.brewkery.domain.usecase

import com.example.brewkery.domain.model.Menu
import com.example.brewkery.domain.repository.MenuRepository
import javax.inject.Inject

class GetMenuUseCase @Inject constructor(private val repository: MenuRepository) {
    suspend operator fun invoke(): Result<Menu> = repository.getMenu()
}