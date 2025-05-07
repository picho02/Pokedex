package com.example.pokedex.data

import com.example.pokedex.data.apiresponse.Pokemon
import com.example.pokedex.data.apiresponse.PokemonList
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiConnection {
    @GET("pokemon")
    suspend fun getList(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int
    ): PokemonList

    @GET("pokemon/{name}")
    suspend fun getPokemonInfo(
        @Path("name") name: String
    ): Pokemon
}