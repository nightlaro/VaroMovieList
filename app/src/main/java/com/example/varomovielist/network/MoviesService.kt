package com.example.varomovielist.network

import com.example.varomovielist.models.NowPlaying
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

private const val API_KEY = "7bfe007798875393b05c5aa1ba26323e"
private const val DEFAULT_LANGUAGE = "&language=en-US"

interface MoviesService {
    @GET("movie/now_playing?api_key=$API_KEY$DEFAULT_LANGUAGE")
    fun getNowPlaying(@Query("page") page: Int): Call<NowPlaying>
}