package com.example.varomovielist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.varomovielist.models.Movie

@Composable
fun MoviesListScreen(viewModel: MainActivityViewmodel) {
    val state by viewModel.stateFlow.collectAsState()

    Column(
        modifier = Modifier.systemBarsPadding(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        if (state.isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(50.dp),
                color = Color.Magenta,
                strokeWidth = 10.dp
            )
        } else {
            AnimatedVisibility(visible = state.shouldShowFavorites) {
                MoviesList(movies = state.favorites, isFavoriteList = true, onFavorite = {
                    viewModel.send(MainActivityViewmodel.Intent.RemoveFavoriteMovie(it))
                })
            }
            AnimatedVisibility(visible = !state.shouldShowFavorites) {
                MoviesList(
                    movies = state.movies,
                    onFavorite = {
                        viewModel.send(MainActivityViewmodel.Intent.AddFavoriteMovie(it))
                    },
                    onLastItem = {
                        viewModel.send(MainActivityViewmodel.Intent.LoadNextPage(state.currentPage + 1))
                    }
                )
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
fun MoviesList(
    movies: List<Movie>,
    isFavoriteList: Boolean = false,
    onFavorite: (Movie) -> Unit,
    onLastItem: () -> Unit = {}
) {
    val lazyListState = rememberLazyListState()
    val lastItemVisible by remember {
        derivedStateOf {
            lazyListState.layoutInfo.visibleItemsInfo.lastOrNull()?.index == movies.lastIndex
        }
    }
    if (lastItemVisible) {
        onLastItem()
    }
    LazyColumn(
        contentPadding = PaddingValues(bottom = 58.dp),
        state = lazyListState
    ) {
        items(movies.size) { index ->
            MovieItem(
                movie = movies[index],
                isFavoriteItem = isFavoriteList,
                onCardClicked = onFavorite
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MovieItem(movie: Movie, isFavoriteItem: Boolean = false, onCardClicked: (Movie) -> Unit) {
    var currentBackgroundColor by remember {
        if (isFavoriteItem) {
            mutableStateOf(Color.Green)
        } else {
            mutableStateOf(Color.LightGray)
        }
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
            if (!isFavoriteItem) {
                currentBackgroundColor = Color.Green
            }
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
            .requiredHeight(100.dp),
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