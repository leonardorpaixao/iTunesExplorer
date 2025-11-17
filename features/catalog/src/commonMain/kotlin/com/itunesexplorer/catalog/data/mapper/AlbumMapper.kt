package com.itunesexplorer.catalog.data.mapper

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.data.models.RssFeedEntry
import com.itunesexplorer.currency.domain.CurrencyFormatter
import com.itunesexplorer.network.models.ITunesItem

/**
 * Maps RssFeedEntry and ITunesItem to Album domain model.
 */
object AlbumMapper {
    /**
     * Convert an RssFeedEntry (from RSS feed) to an Album.
     *
     * @param entry The RSS feed entry
     * @return Album domain model
     */
    fun fromRss(entry: RssFeedEntry): Album {
        // Get the highest resolution image (last in the list)
        val imageUrl = entry.imImage.lastOrNull()?.label

        // Get price label if available
        val price = entry.imPrice?.label

        return Album(
            id = entry.id.attributes.imId,
            name = entry.imName.label,
            artistName = entry.imArtist?.label ?: CatalogConstants.UNKNOWN_ARTIST,
            imageUrl = imageUrl,
            viewUrl = entry.link.attributes.href,
            price = price,
            releaseDate = entry.imReleaseDate?.attributes?.label,
            genre = entry.category.attributes.label
        )
    }

    /**
     * Convert an ITunesItem (from Search API) to an Album.
     * This is used when filtering by genre, as the RSS feed doesn't support genre filtering.
     *
     * @param item The iTunes search result item
     * @return Album domain model, or null if the item doesn't have required collection fields
     */
    fun fromITunesItem(item: ITunesItem): Album? {
        val collectionId = item.collectionId ?: return null
        val collectionName = item.collectionName ?: return null

        // Get the best available image URL
        val imageUrl = item.artworkUrl100 ?: item.artworkUrl60 ?: item.artworkUrl30

        // Format price if available
        val collectionPrice = item.collectionPrice
        val currency = item.currency

        val price = if (collectionPrice != null && currency != null) {
            CurrencyFormatter.format(collectionPrice, currency)
        } else {
            null
        }

        return Album(
            id = collectionId.toString(),
            name = collectionName,
            artistName = item.artistName ?: CatalogConstants.UNKNOWN_ARTIST,
            imageUrl = imageUrl,
            viewUrl = item.collectionViewUrl ?: CatalogConstants.EMPTY_URL,
            price = price,
            releaseDate = item.releaseDate,
            genre = item.primaryGenreName ?: CatalogConstants.DEFAULT_GENRE
        )
    }

    /**
     * Convert a list of RssFeedEntries to Albums.
     */
    fun fromRssList(entries: List<RssFeedEntry>): List<Album> {
        return entries.map { fromRss(it) }
    }

    /**
     * Convert a list of ITunesItems to Albums, filtering out invalid items.
     */
    fun fromITunesItemList(items: List<ITunesItem>): List<Album> {
        return items.mapNotNull { fromITunesItem(it) }
    }
}
