package com.example.mdev1001_m2023_assignment3

import com.squareup.moshi.Json
    data class DataClass(
        @field:Json(name = "Title") val Title: String?,
        @field:Json(name = "Year") val Year: String?,
        @field:Json(name = "Rated") val Rated: String?,
        @field:Json(name = "Released") val Released: String?,
        @field:Json(name = "Runtime") val Runtime: String?,
        @field:Json(name = "Genre") val Genre: String?,
        @field:Json(name = "Director") val Director: String?,
        @field:Json(name = "Writer") val Writer: String?,
        @field:Json(name = "Actors") val Actors: String?,
        @field:Json(name = "Plot") val Plot: String?,
        @field:Json(name = "Poster") val Poster: String?,
        @field:Json(name = "imdbRating") val imdbRating: String?,
        @field:Json(name = "Type") val Type: String?,
        @field:Json(name = "Website") val Website: String?,
        @field:Json(name = "Response") val Response: String?,
        @field:Json(name = "Error") val Error: String   ?
    )

    data class Rating(
        @field:Json(name = "Source") val Source: String,
        @field:Json(name = "Value") val Value: String
    )
