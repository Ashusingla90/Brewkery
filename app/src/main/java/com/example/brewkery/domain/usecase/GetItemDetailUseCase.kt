package com.example.brewkery.domain.usecase

import com.example.brewkery.domain.model.MenuItem
import com.example.brewkery.domain.repository.MenuRepository
import javax.inject.Inject

class GetItemDetailUseCase @Inject constructor(private val repository: MenuRepository) {
    suspend operator fun invoke(id: Int): Result<MenuItem> = repository.getItemDetail(id)
}

