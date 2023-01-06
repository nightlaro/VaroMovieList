package com.example.varomovielist.models

import com.example.varomovielist.data_store.entity.FavoriteMovieEntity
import com.example.varomovielist.data_store.entity.MovieEntity

data class Movie(
    val id: Int,
    val overview: String,
    val popularity: Double,
    val poster_path: String,
    val release_date: String,
    val title: String,
    val vote_average: Double,
    val vote_count: Int
)

fun Movie.toMovieEntity() = MovieEntity(
    id = id,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    vote_average = vote_average,
    vote_count = vote_count
)

fun Movie.toFavoriteEntity() = FavoriteMovieEntity(
    id = id,
    overview = overview,
    popularity = popularity,
    poster_path = poster_path,
    release_date = release_date,
    title = title,
    vote_average = vote_average,
    vote_count = vote_count
)
