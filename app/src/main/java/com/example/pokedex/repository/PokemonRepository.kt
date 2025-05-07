package com.example.pokedex.repository

import com.example.pokedex.data.ApiConnection
import com.example.pokedex.data.apiresponse.Pokemon
import com.example.pokedex.data.apiresponse.PokemonList
import com.example.pokedex.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class PokemonRepository @Inject constructor(
    private val api: ApiConnection
) {
    suspend fun getPokemonList(limit: Int, offset: Int): Resource<PokemonList> {
        val response = try {
            api.getList(limit, offset)
        } catch (e: Exception) {
            return Resource.Error("Error")
        }
        return Resource.Success(response)
    }

    suspend fun getPokemonInfo(pokemonName: String): Resource<Pokemon> {
        val response = try {
            api.getPokemonInfo(pokemonName)
        } catch (e: Exception) {
            return Resource.Error("Error")
        }
        return Resource.Success(response)
    }
}