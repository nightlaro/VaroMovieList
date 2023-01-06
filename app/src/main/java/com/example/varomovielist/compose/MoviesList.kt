package com.example.varomovielist.compose

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.res.dimensionResource
import com.example.varomovielist.R
import com.example.varomovielist.models.Movie

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
        contentPadding = PaddingValues(bottom = dimensionResource(R.dimen.list_padding)),
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