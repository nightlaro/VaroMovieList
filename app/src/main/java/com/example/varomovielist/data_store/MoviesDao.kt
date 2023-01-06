package com.example.varomovielist.data_store

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.varomovielist.data_store.entity.FavoriteMovieEntity
import com.example.varomovielist.data_store.entity.MovieEntity

@Dao
interface MoviesDao {
    @Query("SELECT * FROM movieentity")
    fun getAllCachedMovies(): List<MovieEntity>

    @Query("SELECT * FROM favoritemovieentity")
    fun getAllCachedFavoriteMovies(): List<FavoriteMovieEntity>

    @Insert
    fun insertMovie(vararg movies: MovieEntity)

    @Insert
    fun insertAllFavoriteMovies(vararg movies: FavoriteMovieEntity)

    @Delete
    fun deleteFavoriteMovie(favoriteMovie: FavoriteMovieEntity)
}