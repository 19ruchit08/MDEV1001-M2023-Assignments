package com.example.mdev1001_m2023_assignment3

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApiService {

    @GET("/")
    fun searchMovies(
        @Query("apikey") apiKey: String,
        @Query("s") searchText: String,
    ): Call<MovieSearch>

    @GET("/")
    fun MovieById(
        @Query("apikey") apiKey: String,
        @Query("i") movieId: String,
    ): Call<DataClass>

    @GET("/")
    fun MovieByTitle(
        @Query("apikey") apiKey: String,
        @Query("t") movieId: String,
    ): Call<DataClass>
}