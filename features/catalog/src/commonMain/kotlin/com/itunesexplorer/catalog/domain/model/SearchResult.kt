package com.itunesexplorer.catalog.domain.model

/**
 * Domain model representing a search result item from iTunes Store.
 * Independent of API response structure.
 */
data class SearchResult(
    val id: String,
    val type: String,
    val name: String,
    val artistName: String?,
    val collectionName: String?,
    val imageUrl: String?,
    val viewUrl: String?,
    val previewUrl: String?,
    val price: String?,
    val releaseDate: String?,
    val genre: String?,
    val description: String?
)
