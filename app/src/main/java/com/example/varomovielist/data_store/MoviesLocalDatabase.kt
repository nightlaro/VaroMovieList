package com.example.varomovielist.data_store

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.varomovielist.data_store.entity.FavoriteMovieEntity
import com.example.varomovielist.data_store.entity.MovieEntity

@Database(entities = [MovieEntity::class, FavoriteMovieEntity::class], version = 1)
abstract class MoviesLocalDatabase : RoomDatabase() {
    abstract fun moviesDao(): MoviesDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MoviesLocalDatabase? = null

        fun getDatabase(context: Context): MoviesLocalDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MoviesLocalDatabase::class.java,
                    "movie_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}