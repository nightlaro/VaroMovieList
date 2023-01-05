package com.example.varomovielist.models

data class NowPlaying(
    val dates: NowPlayingDates,
    val page: Int,
    val results: List<Movie>,
    val total_pages: Int,
    val total_results: Int
)

data class NowPlayingDates(
    val maximum: String,
    val minimum: String
)