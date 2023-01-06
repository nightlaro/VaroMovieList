package com.example.varomovielist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.varomovielist.models.Movie
import com.example.varomovielist.repository.MoviesRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewmodel : ViewModel() {
    private val intentSharedFlow = MutableSharedFlow<Intent>()
    private val _stateFlow = MutableStateFlow(State())
    val stateFlow: StateFlow<State>
        get() = _stateFlow.asStateFlow()

    private val moviesRepository = MoviesRepository()
    init {
        intentSharedFlow.onEach {
            val currentState = stateFlow.value
            _stateFlow.value = reduce(currentState, it)
        }.launchIn(viewModelScope)

        moviesRepository.moviesStateFlow.onEach {
            if (it.movies.isNotEmpty()) {
                send(Intent.UpdateMovieList(it.movies))
            }
            if (it.favorites.isNotEmpty()) {
                send(Intent.UpdateFavorites(it.favorites))
            }
        }.launchIn(viewModelScope)
    }

    fun send(intent: Intent) {
        viewModelScope.launch {
            intentSharedFlow.emit(intent)
        }
    }

    private fun reduce(currentState: State, intent: Intent): State {
        Log.d("MainActivityViewmodel", "reduce: $intent")

        return when (intent) {
            is Intent.UpdateMovieList -> {
                currentState.copy(
                    movies = intent.movies,
                    isLoading = false
                )
            }
            is Intent.UpdateFavorites -> {
                currentState.copy(
                    isLoading = false,
                    favorites = intent.favorites
                )
            }
            Intent.ShowFavorites -> {
                currentState.copy(
                    shouldShowFavorites = true
                )
            }
            Intent.ShowHome -> {
                currentState.copy(
                    shouldShowFavorites = false
                )
            }
            is Intent.AddFavoriteMovie -> {
                // save favorite movie locally
                currentState.copy(
                    favorites = (currentState.favorites + intent.movie).distinctBy { it.id }
                )
            }
            is Intent.RemoveFavoriteMovie -> {
                // remove favorite movie locally
                val favoriteMovies = currentState.favorites - intent.movie
                currentState.copy(
                    favorites = favoriteMovies
                )
            }
        }
    }

    data class State(
        val isLoading: Boolean = true,
        val shouldShowFavorites: Boolean = false,
        val movies: List<Movie> = emptyList(),
        val error: String = "",
        val favorites: List<Movie> = emptyList()
    )

    sealed class Intent {
        data class UpdateMovieList(val movies: List<Movie>) : Intent()
        data class UpdateFavorites(val favorites: List<Movie>) : Intent()
        object ShowFavorites : Intent()
        object ShowHome : Intent()
        data class AddFavoriteMovie(val movie: Movie) : Intent()
        data class RemoveFavoriteMovie(val movie: Movie) : Intent()
    }
}