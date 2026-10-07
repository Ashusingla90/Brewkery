package com.example.brewkery.di

import android.content.Context
import androidx.room.Room
import com.example.brewkery.data.local.BrewDatabase
import com.example.brewkery.data.local.CartDao
import com.example.brewkery.data.local.OrderDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideDb(@ApplicationContext context: Context): BrewDatabase =
        Room.databaseBuilder(context, BrewDatabase::class.java, "brewkery.db").build()

    @Provides fun provideCartDao(db: BrewDatabase): CartDao = db.cartDao()
    @Provides fun provideOrderDao(db: BrewDatabase): OrderDao = db.orderDao()
}
