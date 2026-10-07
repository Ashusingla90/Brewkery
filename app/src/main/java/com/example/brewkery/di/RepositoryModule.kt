package com.example.brewkery.di


import com.example.brewkery.data.repository.CartRepositoryImpl
import com.example.brewkery.data.repository.InMemoryCartRepository
import com.example.brewkery.data.repository.InMemoryOrderRepository
import com.example.brewkery.data.repository.MenuRepositoryImpl
import com.example.brewkery.data.repository.OrderRepositoryImpl
import com.example.brewkery.domain.repository.CartRepository
import com.example.brewkery.domain.repository.MenuRepository
import com.example.brewkery.domain.repository.OrderRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds @Singleton
    abstract fun bindMenuRepository(impl: MenuRepositoryImpl): MenuRepository

    @Binds @Singleton
    abstract fun bindCartRepository(impl: CartRepositoryImpl): CartRepository

    @Binds @Singleton
    abstract fun bindOrderRepository(impl: OrderRepositoryImpl): OrderRepository

}