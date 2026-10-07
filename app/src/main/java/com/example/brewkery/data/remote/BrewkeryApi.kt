package com.example.brewkery.data.remote


import retrofit2.http.GET
import retrofit2.http.Path

interface BrewkeryApi {

    @GET("data.json")
    suspend fun getMenu(): MenuResponseDto

    @GET("api/items/{id}.json")
    suspend fun getItemDetail(@Path("id") id: Int): ItemDto
}