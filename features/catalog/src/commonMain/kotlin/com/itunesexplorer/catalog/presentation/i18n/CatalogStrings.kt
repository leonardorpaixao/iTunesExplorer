package com.itunesexplorer.catalog.presentation.i18n

import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.MusicGenre

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
    val changeRegionHint: String,
    val january: String,
    val february: String,
    val march: String,
    val april: String,
    val may: String,
    val june: String,
    val july: String,
    val august: String,
    val september: String,
    val october: String,
    val november: String,
    val december: String
)
