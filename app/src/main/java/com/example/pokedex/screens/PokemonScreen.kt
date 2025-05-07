package com.example.pokedex.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedex.data.models.PokedexListEntry

@Composable
fun PokemonScreen(
    navController: NavController,
    viewModel: PokemonListViewModel = hiltViewModel()
) {
    val pokemonList by remember { viewModel.pokemonList }
    val isLoading by remember { viewModel.isLoading }
    val loadError by remember { viewModel.loadError }
    LazyColumn {
        items(pokemonList) { pokemon ->
            PokemonRow(pokemon, navController)
        }
    }
    Box(
        contentAlignment = Center,
        modifier = Modifier.fillMaxSize()
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = Color.Red)
        }
        if (loadError.isNotEmpty()) {
            RetrySection(
                error = loadError
            ) {
                viewModel.loadPokemonPaginated()
            }
        }
    }
}

@Composable
fun PokemonRow(
    pokemon: PokedexListEntry,
    navController: NavController,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable {
                navController.navigate(
                    "PokemonDetailScreen/${pokemon.pokemonName}"
                )
            }
    ) {
        Column {
            Row {
                Surface(
                    modifier.size(120.dp),
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f)
                ) {
                    AsyncImage(
                        model = pokemon.imageUrl,
                        contentDescription = pokemon.pokemonName,
                        contentScale = ContentScale.FillBounds,
                        modifier = Modifier
                            .size(120.dp)
                    )
                }
                Column(
                    modifier
                        .padding(16.dp)
                        .align(Alignment.CenterVertically)
                        .weight(1f)
                ) {
                    Text(
                        text = pokemon.pokemonName,
                        style = MaterialTheme.typography.titleLarge
                    )
                }
            }
        }
    }

}

@Composable
fun RetrySection(
    error: String,
    onRetry: () -> Unit
) {
    Column {
        Text(
            error, color = Color.Red,
            fontSize = 18.sp,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = {
                onRetry()
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)
        ) {
            Text(text = "Retry")
        }
    }
}

