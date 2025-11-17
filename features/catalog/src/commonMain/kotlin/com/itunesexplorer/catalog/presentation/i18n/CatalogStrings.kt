package com.itunesexplorer.catalog.presentation.i18n

import com.itunesexplorer.network.models.MediaType
import com.itunesexplorer.network.models.MusicGenre

data class CatalogStrings(
    val search: String,
    val searchPlaceholder: String,
    val noResults: String,
    val errorLoading: String,
    val retry: String,
    val topAlbums: String,
    val topAlbumsDescription: String,
    val surpriseMe: String,
    val mediaTypeChip: (MediaType) -> String,
    val musicGenreChip: (MusicGenre) -> String,
    val details: String,
    val noDetails: String,
    val relatedItems: String,
    val genre: String,
    val price: String,
    val releaseDate: String,
    val trackCount: String,
    val openInStore: String,
    val back: String,
    val changeRegionHint: String
)
