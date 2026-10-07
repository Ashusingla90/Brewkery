package com.example.brewkery.domain.repository

import com.example.brewkery.domain.model.Menu
import com.example.brewkery.domain.model.MenuItem
import com.example.brewkery.domain.model.StoreInfo
import kotlinx.coroutines.flow.StateFlow

interface MenuRepository {
    val store: StateFlow<StoreInfo?>
    suspend fun getMenu(): Result<Menu>
    suspend fun getItemDetail(id: Int): Result<MenuItem>
}