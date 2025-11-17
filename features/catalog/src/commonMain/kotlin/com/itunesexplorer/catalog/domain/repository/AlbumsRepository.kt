package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.model.MusicGenre
import com.itunesexplorer.core.common.domain.DomainResult

/**
 * Repository interface for fetching album data from iTunes Store.
 * Handles both RSS feeds and search API, abstracting the data source selection.
 */
interface AlbumsRepository {
    /**
     * Get top albums for the user's current country.
     * Automatically uses RSS feed when available, falling back to search API.
     *
     * @param limit Maximum number of albums to fetch
     * @return DomainResult containing list of albums or a domain error
     */
    suspend fun getTopAlbums(limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT): DomainResult<List<Album>>

    /**
     * Get albums filtered by music genre.
     * Uses search API with genre-specific search terms.
     *
     * @param genre The music genre to filter by
     * @param limit Maximum number of albums to fetch
     * @return DomainResult containing list of albums or a domain error
     */
    suspend fun getAlbumsByGenre(
        genre: MusicGenre,
        limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT
    ): DomainResult<List<Album>>
}
