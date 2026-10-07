package com.example.brewkery.di

import com.example.brewkery.data.remote.BrewkeryApi
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL =
        "https://raw.githubusercontent.com/VivekShah138/Brewkery/main/"

    @Provides @Singleton
    fun provideMoshi(): Moshi =
        Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()

    @Provides @Singleton
    fun provideRetrofit(moshi: Moshi): Retrofit =
        Retrofit.Builder()
            .baseUrl(BASE_URL) // must end with "/"
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()

    @Provides @Singleton
    fun provideApi(retrofit: Retrofit): BrewkeryApi =
        retrofit.create(BrewkeryApi::class.java)
}