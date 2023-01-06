package com.example.varomovielist

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.varomovielist.compose.MoviesList
import com.example.varomovielist.compose.NavigationBar

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
            AnimatedVisibility(
                visible = state.shouldShowFavorites,
                enter = slideInHorizontally(
                    animationSpec = tween(delayMillis = 250),
                    initialOffsetX = { it }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it }
                )
            ) {
                MoviesList(movies = state.favorites, isFavoriteList = true, onFavorite = {
                    viewModel.send(MainActivityViewmodel.Intent.RemoveFavoriteMovie(it))
                })
            }
            AnimatedVisibility(
                visible = !state.shouldShowFavorites,
                enter = slideInHorizontally(
                    animationSpec = tween(delayMillis = 250),
                    initialOffsetX = { it }
                ),
                exit = slideOutHorizontally(
                    targetOffsetX = { -it }
                )
            ) {
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
