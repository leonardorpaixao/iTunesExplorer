package com.itunesexplorer.home.i18n

import com.itunesexplorer.network.models.MediaType

data class HomeStrings(
    val appName: String,
    val search: String,
    val searchPlaceholder: String,
    val noResults: String,
    val errorLoading: String,
    val retry: String,
    val topAlbums: String,
    val topAlbumsDescription: String,
    val surpriseMe: String,
    val mediaTypeChip: (MediaType) -> String,
    // Bottom Navigation Tabs
    val tabAlbums: String,
    val tabSearch: String,
    val tabPreferences: String
)
