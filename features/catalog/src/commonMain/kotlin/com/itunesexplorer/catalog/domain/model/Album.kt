package com.itunesexplorer.catalog.domain.model

/**
 * Domain model representing an album in the iTunes Store.
 * Used for top albums listing and genre-filtered results.
 */
data class Album(
    val id: String,
    val name: String,
    val artistName: String,
    val imageUrl: String?,
    val viewUrl: String,
    val price: Money?,
    val releaseDate: String?,
    val genre: String
)
