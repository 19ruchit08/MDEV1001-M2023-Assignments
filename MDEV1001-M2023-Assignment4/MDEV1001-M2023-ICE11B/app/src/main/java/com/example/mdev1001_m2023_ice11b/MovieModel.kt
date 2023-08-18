package com.example.mdev1001_m2023_ice11b

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

data class Movie(
    @field:Json(name = "documentID") val documentID: String?,
    @field:Json(name = "_id") val _id: String?,
    @field:Json(name = "movieID") val movieID: String,
    @field:Json(name = "title") val title: String,
    @field:Json(name = "studio") val studio: String,
    @field:Json(name = "genres") val genres: List<String>,
    @field:Json(name = "directors") val directors: List<String>,
    @field:Json(name = "writers") val writers: List<String>,
    @field:Json(name = "actors") val actors: List<String>,
    @field:Json(name = "year") val year: Int,
    @field:Json(name = "length") val length: Int,
    @field:Json(name = "shortDescription") val shortDescription: String,
    @field:Json(name = "mpaRating") val mpaRating: String,
    @field:Json(name = "criticsRating") val criticsRating: Double,
    @field:Json(name = "image") val image: String,
)

@JsonClass(generateAdapter = true)
data class UpdatedResponse(
    @Json(name = "lastUpdated") val lastUpdated: Long
)

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "success") val success: Boolean,
    @Json(name = "token") val token: String?,
    @Json(name = "msg") val msg: String?
)
