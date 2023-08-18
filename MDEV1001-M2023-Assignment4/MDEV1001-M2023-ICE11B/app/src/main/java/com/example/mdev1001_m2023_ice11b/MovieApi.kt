package com.example.mdev1001_m2023_ice11b

import retrofit2.Call
import retrofit2.http.*

interface MovieApi {
    @GET("list")
    fun getMovies(): Call<List<Movie>>

    @POST("add")
    fun addMovie(@Body movie: Movie): Call<Void>

    @DELETE("delete/{id}")
    fun deleteMovie(@Path("id") id: String) : Call<Void>

    @PUT("update/{id}")
    fun updateMovie(@Path("id") id: String, @Body movie: Movie): Call<Void>

    @GET("has-updates")
    fun checkForUpdates(): Call<UpdatedResponse>

    @POST("login")
    fun loginUser(@Body credentials: Map<String, String>): Call<LoginResponse>
}