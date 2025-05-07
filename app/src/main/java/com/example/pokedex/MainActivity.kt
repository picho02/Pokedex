package com.example.pokedex

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.remember
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.pokedex.screens.PokemonDetailScreen
import com.example.pokedex.screens.PokemonScreen
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "PokemonScreen") {
                composable("PokemonScreen") {
                    PokemonScreen(navController = navController)
                }
                composable(
                    "PokemonDetailScreen/{pokemonName}",
                    arguments = listOf(
                        navArgument("pokemonName") {
                            type = NavType.StringType
                        }
                    )) {
                    val pokemonName = remember {
                        it.arguments?.getString("pokemonName")
                    }
                    PokemonDetailScreen(
                        pokemonName = pokemonName?.lowercase(Locale.ROOT) ?: "",
                        navController = navController
                    )
                }
            }
        }
    }
}
