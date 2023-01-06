package com.example.varomovielist.compose

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.varomovielist.models.Movie

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
