package com.example.varomovielist.repository

import com.example.varomovielist.models.Movie
import com.example.varomovielist.network.MoviesAPIClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MoviesRepository {
    private val scope = MainScope() + SupervisorJob()
    private val moviesAPIService = MoviesAPIClient().moviesService
    private val _moviesStateFlow = MutableStateFlow(MoviesState())
    val moviesStateFlow: StateFlow<MoviesState>
        get() = _moviesStateFlow.asStateFlow()

    init {
        loadNowPlayingMovieByPage()
    }

    fun loadNowPlayingMovieByPage(page: Int = 1) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val nowPlaying = moviesAPIService.getNowPlaying(page).execute()
                if (nowPlaying.isSuccessful) {
                    val movies = nowPlaying.body()?.results ?: emptyList()
                    _moviesStateFlow.value = _moviesStateFlow.value.copy(movies = movies)
                }
            }
        }
    }

    data class MoviesState(
        val movies: List<Movie> = emptyList(),
        val error: String = "",
        val favorites: List<Movie> = emptyList()
    )
}