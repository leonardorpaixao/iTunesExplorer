package com.itunesexplorer.catalog.domain.model

/**
 * Domain model representing detailed information about an iTunes Store item.
 * Separates the main item from related items (e.g., album tracks).
 */
data class ItemDetails(
    val mainItem: SearchResult,
    val relatedItems: List<SearchResult>,
)
