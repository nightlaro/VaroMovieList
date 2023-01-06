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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import com.example.varomovielist.R
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
        modifier = Modifier.padding(dimensionResource(R.dimen.movie_card_padding)),
        shape = RoundedCornerShape(dimensionResource(R.dimen.movie_card_round_radius)),
        backgroundColor = animatedColor.value,
        elevation = dimensionResource(R.dimen.movie_card_elevation),
        onClick = {
            if (!isFavoriteItem) {
                currentBackgroundColor = Color.Green
            }
            onCardClicked(movie)
        }
    ) {
        Column(
            modifier = Modifier.padding(
                horizontal = dimensionResource(R.dimen.movie_card_content_horizontal_padding),
                vertical = dimensionResource(R.dimen.movie_card_content_vertical_padding)
            ),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val posterURL = "https://image.tmdb.org/t/p/original/${movie.poster_path}"
            AsyncImage(
                model = posterURL,
                contentDescription = movie.title,
                modifier = Modifier.size(dimensionResource(R.dimen.movie_poster_size))
            )
            Text(text = movie.title)
            Text(text = "${stringResource(R.string.release_date)}${movie.release_date}")
            Text(text = "${stringResource(R.string.rating)}${movie.vote_average}")
            Text(text = "${stringResource(R.string.popularity)}${movie.popularity}")
            Text(text = stringResource(R.string.overview))
            Text(text = movie.overview)
        }
    }
}
