package com.example.varomovielist.repository

import com.example.varomovielist.data_store.MoviesDao
import com.example.varomovielist.data_store.entity.toMovie
import com.example.varomovielist.models.Movie
import com.example.varomovielist.models.toFavoriteEntity
import com.example.varomovielist.models.toMovieEntity
import com.example.varomovielist.network.MoviesAPIClient
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class MoviesRepository(private val moviesDao: MoviesDao) {
    private val scope = MainScope() + SupervisorJob()
    private val moviesAPIService = MoviesAPIClient().moviesService
    private val _moviesStateFlow = MutableStateFlow(MoviesState())
    val moviesStateFlow: StateFlow<MoviesState>
        get() = _moviesStateFlow.asStateFlow()

    init {
        // Load cached movies if there are any
        scope.launch {
            withContext(Dispatchers.IO) {
                val cachedMovies = moviesDao.getAllCachedMovies()
                if (cachedMovies.isNotEmpty()) {
                    val entityToMovie = cachedMovies.map { it.toMovie() }
                    setNowPlayingMovies(entityToMovie)
                } else {
                    loadNowPlayingMovieByPage()
                }

                // Load cached favorites if there are any
                val cachedFavorites = moviesDao.getAllCachedFavoriteMovies()
                if (cachedFavorites.isNotEmpty()) {
                    val entityFavoriteToMovie = cachedFavorites.map { it.toMovie() }
                    setFavorites(entityFavoriteToMovie)
                }
            }
        }
    }

    fun loadNowPlayingMovieByPage(page: Int = 1) {
        scope.launch {
            withContext(Dispatchers.IO) {
                val nowPlaying = moviesAPIService.getNowPlaying(page).execute()
                if (nowPlaying.isSuccessful) {
                    val movies = nowPlaying.body()?.results ?: emptyList()
                    setAndCacheNowPlayingMovies(movies)
                }
            }
        }
    }

    fun saveFavoriteToDB(movie: Movie) {
        scope.launch {
            withContext(Dispatchers.IO) {
                moviesDao.insertAllFavoriteMovies(movie.toFavoriteEntity())
            }
        }
    }

    fun removeFavoriteFromDB(movie: Movie) {
        scope.launch {
            withContext(Dispatchers.IO) {
                moviesDao.deleteFavoriteMovie(movie.toFavoriteEntity())
            }
        }
    }

    private fun setAndCacheNowPlayingMovies(movies: List<Movie>) {
        scope.launch {
            withContext(Dispatchers.IO) {
                movies.forEach { movie ->
                    if (moviesDao.getCachedMovieById(movie.id) == null) {
                        moviesDao.insertMovie(movie.toMovieEntity())
                    }
                }
            }
        }
        setNowPlayingMovies(movies)
    }

    private fun setNowPlayingMovies(movies: List<Movie>) {
        _moviesStateFlow.value = _moviesStateFlow.value.copy(movies = movies)
    }

    private fun setFavorites(favorites: List<Movie>) {
        _moviesStateFlow.value = _moviesStateFlow.value.copy(favorites = favorites)
    }

    data class MoviesState(
        val movies: List<Movie> = emptyList(),
        val error: String = "",
        val favorites: List<Movie> = emptyList()
    )
}