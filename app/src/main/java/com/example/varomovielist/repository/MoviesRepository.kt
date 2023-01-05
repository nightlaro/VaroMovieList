package com.example.varomovielist.repository

import com.example.varomovielist.models.Movie
import com.example.varomovielist.network.MoviesAPIClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MoviesRepository {
    private val moviesAPIService = MoviesAPIClient().moviesService
    private val _moviesStateFlow = MutableStateFlow(MoviesState())
    val moviesStateFlow: StateFlow<MoviesState>
        get() = _moviesStateFlow.asStateFlow()

    init {
        val nowPlayingMovies = moviesAPIService.getNowPlaying().results

        _moviesStateFlow.value = MoviesState(
            movies = nowPlayingMovies
        )
    }

    data class MoviesState(
        val movies: List<Movie> = emptyList(),
        val error: String = "",
        val favorites: List<Movie> = emptyList()
    )
}