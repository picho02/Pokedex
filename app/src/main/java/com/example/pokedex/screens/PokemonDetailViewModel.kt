package com.example.pokedex.screens

import androidx.lifecycle.ViewModel
import com.example.pokedex.data.apiresponse.Form
import com.example.pokedex.data.apiresponse.Pokemon
import com.example.pokedex.data.apiresponse.Sprites
import com.example.pokedex.repository.PokemonRepository
import com.example.pokedex.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PokemonDetailViewModel @Inject constructor(
    private val repository: PokemonRepository
) : ViewModel() {

    suspend fun loadPokemonDetail(pokemonName: String): Resource<Pokemon> {
        return repository.getPokemonInfo(pokemonName)
    }

    fun generateFormsList(pokemon: Sprites): List<Form> {
        var pokemonFormsList = mutableListOf<Form>()
        if (pokemon.front_default != null) {
            pokemonFormsList += Form("Front male", pokemon.front_default)
            pokemonFormsList += Form("back male", pokemon.back_default)
        }
        if (pokemon.front_female != null) {
            pokemonFormsList += Form("Front female", pokemon.front_female.toString())
            pokemonFormsList += Form("back female", pokemon.back_female.toString())
        }
        if (pokemon.front_shiny != null) {
            pokemonFormsList += Form("Front shiny male", pokemon.front_shiny)
            pokemonFormsList += Form("back shine male", pokemon.back_shiny)
        }
        if (pokemon.front_shiny_female != null) {
            pokemonFormsList += Form("Front shiny female", pokemon.front_shiny_female.toString())
            pokemonFormsList += Form("back shine female", pokemon.back_shiny_female.toString())
        }
        return pokemonFormsList
    }
}