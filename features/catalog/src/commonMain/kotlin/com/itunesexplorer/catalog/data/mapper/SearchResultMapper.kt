package com.itunesexplorer.catalog.data.mapper

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.network.models.ITunesItem

/**
 * Maps ITunesItem (network model) to SearchResult (domain model).
 */
object SearchResultMapper {
    /**
     * Convert an ITunesItem to a SearchResult.
     * Handles different item types (track, collection, etc.) and selects appropriate fields.
     *
     * @param item The ITunesItem from the API
     * @return SearchResult domain model
     */
    fun toDomain(item: ITunesItem): SearchResult {
        // Determine the item ID (prefer trackId, fallback to collectionId, then artistId)
        val id = item.trackId?.toString()
            ?: item.collectionId?.toString()
            ?: item.artistId?.toString()
            ?: CatalogConstants.UNKNOWN_ID

        // Determine the name based on item type
        val name = item.trackName ?: item.collectionName ?: item.artistName ?: CatalogConstants.UNKNOWN_NAME

        // Get the best available image URL (prefer higher resolution)
        val imageUrl = item.artworkUrl100 ?: item.artworkUrl60 ?: item.artworkUrl30

        // Get the view URL (prefer track, fallback to collection)
        val viewUrl = item.trackViewUrl ?: item.collectionViewUrl ?: item.artistViewUrl

        // Create Money value object if price is available
        val price = when {
            item.trackPrice != null && item.currency != null -> {
                Money.fromOptional(item.trackPrice, item.currency)
            }
            item.collectionPrice != null && item.currency != null -> {
                Money.fromOptional(item.collectionPrice, item.currency)
            }
            else -> null
        }

        // Get description (for podcasts, audiobooks, etc.)
        val description = item.longDescription ?: item.shortDescription

        return SearchResult(
            id = id,
            type = item.kind ?: item.wrapperType ?: CatalogConstants.UNKNOWN_ID,
            name = name,
            artistName = item.artistName,
            collectionName = item.collectionName,
            imageUrl = imageUrl,
            viewUrl = viewUrl,
            previewUrl = item.previewUrl,
            price = price,
            releaseDate = item.releaseDate,
            genre = item.primaryGenreName,
            description = description
        )
    }

    /**
     * Convert a list of ITunesItems to SearchResults.
     */
    fun toDomainList(items: List<ITunesItem>): List<SearchResult> {
        return items.map { toDomain(it) }
    }
}
