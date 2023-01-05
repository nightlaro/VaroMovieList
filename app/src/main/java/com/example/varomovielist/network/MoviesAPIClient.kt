package com.example.varomovielist.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MoviesAPIClient {
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.themoviedb.org/3/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val moviesService: MoviesService = retrofit.create(MoviesService::class.java)
}