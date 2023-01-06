package com.example.varomovielist

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.varomovielist.models.Movie

@Composable
fun MoviesListScreen() {
    val viewModel: MainActivityViewmodel = viewModel()
    val state by viewModel.stateFlow.collectAsState()

    if (state.isLoading) {
        CircularProgressIndicator()
    } else {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            if (state.shouldShowFavorites) {
                MoviesList(modifier = Modifier.fillMaxSize(), movies = state.favorites) {
                    viewModel.send(MainActivityViewmodel.Intent.RemoveFavoriteMovie(it))
                }
            } else {
                MoviesList(modifier = Modifier.fillMaxSize(), movies = state.movies) {
                    viewModel.send(MainActivityViewmodel.Intent.AddFavoriteMovie(it))
                }
            }
            NavigationBar(
                onFavoriteClicked = {
                    viewModel.send(MainActivityViewmodel.Intent.ShowFavorites)
                },
                onHomeClicked = {
                    viewModel.send(MainActivityViewmodel.Intent.ShowHome)
                }
            )
        }
    }
}

@Composable
fun MoviesList(modifier: Modifier, movies: List<Movie>, onFavorite: (Movie) -> Unit) {
    LazyColumn() {
        items(movies.size) { index ->
            MovieItem(movie = movies[index], onCardClicked = onFavorite)
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieItem(movie: Movie, onCardClicked: (Movie) -> Unit) {
    var currentBackgroundColor by remember {
        mutableStateOf(Color.LightGray)
    }
    val animatedColor = animateColorAsState(
        targetValue = currentBackgroundColor,
        animationSpec = tween(durationMillis = 500)
    )
    Card(
        modifier = Modifier.padding(12.dp),
        shape = RoundedCornerShape(8.dp),
        backgroundColor = animatedColor.value,
        elevation = 8.dp,
        onClick = {
            currentBackgroundColor = Color.Green
            onCardClicked(movie)
        }
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val posterURL = "https://image.tmdb.org/t/p/original/${movie.poster_path}"
            AsyncImage(
                model = posterURL,
                contentDescription = movie.title,
                modifier = Modifier.size(200.dp)
            )
            Text(text = movie.title)
            Text(text = "Release date: ${movie.release_date}")
            Text(text = "Rating: ${movie.vote_average}")
            Text(text = "Popularity: ${movie.popularity}")
            Text(text = "Overview:")
            Text(text = movie.overview)
        }
    }
}

@Composable
fun NavigationBar(onFavoriteClicked: () -> Unit, onHomeClicked: () -> Unit) {
    BottomNavigation(
        modifier = Modifier
            .fillMaxWidth()
            .requiredHeight(200.dp),
        backgroundColor = MaterialTheme.colors.primary,
        contentColor = MaterialTheme.colors.onSecondary,
        elevation = 12.dp
    ) {
        Button(
            onClick = onHomeClicked
        ) {
            Text(text = "Home")
        }
        Button(
            onClick = onFavoriteClicked
        ) {
            Text(text = "Favorites")
        }
    }
}