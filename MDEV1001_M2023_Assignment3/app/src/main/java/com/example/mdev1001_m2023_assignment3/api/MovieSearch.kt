package com.example.mdev1001_m2023_assignment3

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Search(
    @Json(name = "Title") val title: String?,
    @Json(name = "Year") val year: String?,
    @Json(name = "imdbID") val imdbId: String?,
    @Json(name = "Type") val type: String?,
    @Json(name = "Poster") val posterUrl: String?
)

@JsonClass(generateAdapter = true)
data class MovieSearch(
    @Json(name = "Search") val Search: List<Search>?  ,
    @Json(name = "totalResults") val totalResults: String?,
    @Json(name = "Response") val response: String?
)

