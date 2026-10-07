package com.example.brewkery.data.repository


import com.example.brewkery.data.mapper.toDomain
import com.example.brewkery.data.remote.BrewkeryApi
import com.example.brewkery.domain.model.Menu
import com.example.brewkery.domain.model.MenuItem
import com.example.brewkery.domain.model.StoreInfo
import com.example.brewkery.domain.repository.MenuRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

class MenuRepositoryImpl @Inject constructor(
    private val api: BrewkeryApi
) : MenuRepository {

    private val _store = MutableStateFlow<StoreInfo?>(null)
    override val store: StateFlow<StoreInfo?> = _store.asStateFlow()

    override suspend fun getMenu(): Result<Menu> =
        safeCall { api.getMenu().toDomain() }
            .onSuccess { _store.value = it.store }

    override suspend fun getItemDetail(id: Int): Result<MenuItem> =
        safeCall { api.getItemDetail(id).toDomain() }

    // runCatching would swallow CancellationException and break coroutine cancellation
    private inline fun <T> safeCall(block: () -> T): Result<T> =
        try {
            Result.success(block())
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Result.failure(e)
        }
}