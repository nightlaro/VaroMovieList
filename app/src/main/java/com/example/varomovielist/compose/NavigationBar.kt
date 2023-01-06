package com.example.varomovielist.compose

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.material.BottomNavigation
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

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