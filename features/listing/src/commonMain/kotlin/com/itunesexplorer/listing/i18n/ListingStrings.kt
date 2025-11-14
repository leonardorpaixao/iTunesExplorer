package com.itunesexplorer.listing.i18n

import com.itunesexplorer.network.models.MediaType

data class ListingStrings(
    val appName: String,
    val search: String,
    val searchPlaceholder: String,
    val noResults: String,
    val errorLoading: String,
    val retry: String,
    val topAlbums: String,
    val topAlbumsDescription: String,
    val surpriseMe: String,
    val mediaTypeChip: (MediaType) -> String
)
