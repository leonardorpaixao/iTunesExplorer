package com.itunesexplorer.catalog.data

/**
 * Constants used throughout the catalog feature module.
 */
object CatalogConstants {

    // API Limits
    const val DEFAULT_SEARCH_LIMIT = 50
    const val DEFAULT_ALBUMS_LIMIT = 30

    // Image sizes for URL transformation
    const val THUMBNAIL_SIZE = "100x100"
    const val FULL_SIZE = "600x600"

    // Default values
    const val DEFAULT_COUNTRY_CODE = "us"

    // Fallback strings
    const val UNKNOWN_ID = "unknown"
    const val UNKNOWN_NAME = "Unknown"
    const val UNKNOWN_ARTIST = "Unknown Artist"
    const val DEFAULT_GENRE = "Music"
    const val EMPTY_URL = ""
}
