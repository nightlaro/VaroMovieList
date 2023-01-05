package com.example.varomovielist

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.varomovielist.models.Movie
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class MainActivityViewmodel : ViewModel() {
    private val intentSharedFlow = MutableSharedFlow<Intent>()
    private val _stateFlow = MutableStateFlow(State())
    val stateFlow: StateFlow<State>
        get() = _stateFlow.asStateFlow()

    init {
        intentSharedFlow.onEach {
            val currentState = stateFlow.value
            _stateFlow.value = reduce(currentState, it)
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
            Intent.FetchMovies -> TODO()
        }
    }

    data class State(
        val isLoading: Boolean = true,
        val movies: List<Movie> = emptyList(),
        val error: String = "",
        val favorites: List<Movie> = emptyList()
    )

    sealed class Intent {
        object FetchMovies : Intent()
    }
}