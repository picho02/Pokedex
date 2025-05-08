package com.example.pokedex.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.pokedex.R
import com.example.pokedex.data.apiresponse.Pokemon
import com.example.pokedex.data.apiresponse.Type
import com.example.pokedex.util.Resource
import java.util.Locale
import kotlin.math.round

@Composable
fun PokemonDetailScreen(
    pokemonName: String,
    navController: NavController,
    topPadding: Dp = 20.dp,
    pokemonImageSize: Dp = 200.dp,
    viewModel: PokemonDetailViewModel = hiltViewModel()
) {
    val pokemonInfo = produceState<Resource<Pokemon>>(initialValue = Resource.Loading()) {
        value = viewModel.loadPokemonDetail(pokemonName)
    }.value

    Surface(
        color =  Color(255,189,181), modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            PokemonDetailTopSection(
                navController
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 16.dp)
            ) {
                PokemonDetailStateWraper(
                    pokemonInfo = pokemonInfo,
                    modifier = Modifier
                        .fillMaxWidth()
                        .size(400.dp)
                        .padding(
                            top = topPadding + pokemonImageSize / 2f,
                            start = 16.dp,
                            bottom = 16.dp,
                            end = 16.dp
                        )
                        .shadow(10.dp, RoundedCornerShape(10.dp))
                        .clip(RoundedCornerShape(10.dp))
                        .background(color = Color(238,249,251))
                        .padding(16.dp),
                    loadingModifier = Modifier
                        .size(100.dp)
                        .padding(
                            top = topPadding + pokemonImageSize / 2f,
                            start = 16.dp,
                            bottom = 16.dp,
                            end = 16.dp
                        )
                )
                Box(
                    contentAlignment = Alignment.TopCenter,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 16.dp, bottom = 16.dp)
                ) {
                    if (pokemonInfo is Resource.Success) {
                        pokemonInfo.data?.sprites?.let {
                            AsyncImage(
                                model = it.front_default,
                                contentDescription = pokemonInfo.data.name,
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier
                                    .size(pokemonImageSize)
                                    .offset(y = topPadding)
                                    .padding(horizontal = 20.dp)
                            )
                        }
                    }
                }
            }
            if (pokemonInfo is Resource.Success) {
                val formList = viewModel.generateFormsList(pokemonInfo.data!!.sprites)
                Text(
                    text = "Forms",
                    fontSize = 25.sp,
                    modifier = Modifier
                        .padding(16.dp)
                )
                LazyRow{
                    items(items = formList) {
                        PokemonFormsEntry(
                            entry = it.url, description = it.name
                        )
                    }
                }
            }
        }
    }

}

@Composable
fun PokemonDetailTopSection(
    navController: NavController
) {
    Icon(
        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
        contentDescription = null,
        tint = Color.Gray,
        modifier = Modifier
            .size(36.dp)
            .offset(16.dp, 20.dp)
            .clickable {
                navController.popBackStack()
            })
}

@Composable
fun PokemonDetailStateWraper(
    pokemonInfo: Resource<Pokemon>,
    modifier: Modifier = Modifier,
    loadingModifier: Modifier = Modifier
) {
    when (pokemonInfo) {
        is Resource.Success -> {
            PokemonDetailSection(
                pokemonInfo.data!!, modifier = modifier.offset(y = (-20).dp)
            )
        }

        is Resource.Error -> {
            Text(
                text = pokemonInfo.message!!, color = Color.Red, modifier = modifier
            )
        }

        is Resource.Loading -> {
            CircularProgressIndicator(
                color = Color.Blue, modifier = loadingModifier
            )
        }

    }
}

@Composable
fun PokemonDetailSection(
    pokemonInfo: Pokemon, modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxSize()
            .offset(y = 100.dp)
            .verticalScroll(scrollState)
    ) {
        Text(
            text = "#${pokemonInfo.id} ${
            pokemonInfo.name.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.ROOT
                ) else it.toString()
            }
        }",
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = Color.Black,
            fontSize = 25.sp
        )
        PokemonTypeSection(types = pokemonInfo.types)
        PokemonDetailDataSection(
            pokemonWeight = pokemonInfo.weight, pokemonHeight = pokemonInfo.height
        )
    }
}

@Composable
fun PokemonTypeSection(
    types: List<Type>
) {
    Row(
        verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(16.dp)
    ) {
        for (type in types) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 8.dp)
                    .clip(CircleShape)
                    .background(Color(249,249,170))
                    .height(35.dp)
            ) {
                Text(
                    text = type.type.name.replaceFirstChar {
                        if (it.isLowerCase()) it.titlecase(
                            Locale.ROOT
                        ) else it.toString()
                    }, color = Color.Black, fontSize = 20.sp
                )
            }
        }
    }
}

@Composable
fun PokemonDetailDataSection(
    pokemonWeight: Int, pokemonHeight: Int, sectionHeight: Dp = 80.dp
) {
    val pokemonWeightInKg = remember {
        round(pokemonWeight * 100f) / 1000f
    }
    val pokemonHeightInM = remember {
        round(pokemonHeight * 100f) / 1000f
    }
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        PokemonDetailDataItem(
            dataValue = pokemonWeightInKg,
            dataUnit = "kg",
            dataIcon = painterResource(id = R.drawable.ic_weight),
            modifier = Modifier.weight(1f)
        )
        Spacer(
            modifier = Modifier
                .size(1.dp, sectionHeight)
                .background(Color.LightGray)
        )
        PokemonDetailDataItem(
            dataValue = pokemonHeightInM,
            dataUnit = "m",
            dataIcon = painterResource(id = R.drawable.ic_height),
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun PokemonDetailDataItem(
    dataValue: Float, dataUnit: String, dataIcon: Painter, modifier: Modifier = Modifier
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier
    ) {
        Icon(
            painter = dataIcon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "$dataValue$dataUnit", color = MaterialTheme.colorScheme.onSurface,
            fontSize = 20.sp
        )
    }
}

@Composable
fun PokemonFormsEntry(
    entry: String,
    description: String,
    modifier: Modifier = Modifier
) {
    Box(
        contentAlignment = Alignment.Center, modifier = modifier
            //.shadow(5.dp, RoundedCornerShape(10.dp))
            .clip(RoundedCornerShape(10.dp))
            .padding(10.dp)
            .fillMaxWidth()
            .height(180.dp)
            .aspectRatio(1f)
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(249,249,170), Color.White
                    )
                )
            )
    ) {
        Column {
            AsyncImage(
                model = entry,
                contentDescription = description,
                modifier = Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
            )
            Text(
                text = description,
                fontSize = 25.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

        }
    }

}
