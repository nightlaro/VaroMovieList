package com.example.varomovielist

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
                MoviesList(modifier = Modifier.fillMaxSize(), movies = state.favorites)
            } else {
                MoviesList(modifier = Modifier.fillMaxSize(), movies = state.movies)
            }
            NavigationBar(onFavoriteClicked = { /*TODO*/ }, onHomeClicked = { /*TODO*/ })
        }
    }
}

@Composable
fun MoviesList(modifier: Modifier, movies: List<Movie>) {
    LazyColumn() {
        items(movies.size) { index ->
            MovieItem(movie = movies[index])
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieItem(movie: Movie) {
    Card(shape = RoundedCornerShape(8.dp), onClick = { /*TODO*/ }) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            AsyncImage(
                model = movie.poster_path,
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
fun SearchBar() {

}

@Composable
fun NavigationBar(onFavoriteClicked: () -> Unit, onHomeClicked: () -> Unit) {
    BottomNavigation {
        Button(onClick = onHomeClicked) {
            Text(text = "Home")
        }
        Spacer(modifier = Modifier.size(8.dp))
        Button(onClick = onFavoriteClicked) {
            Text(text = "Favorites")
        }
    }
}