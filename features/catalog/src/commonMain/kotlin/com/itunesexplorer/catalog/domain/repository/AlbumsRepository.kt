package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.core.common.domain.DomainResult
import com.itunesexplorer.network.models.MusicGenre

/**
 * Repository interface for fetching album data from iTunes Store.
 * Handles both RSS feeds and search API, abstracting the data source selection.
 */
interface AlbumsRepository {
    /**
     * Get top albums for the user's current country.
     * Automatically uses RSS feed when available, falling back to search API.
     *
     * @param limit Maximum number of albums to fetch (default: 30)
     * @return DomainResult containing list of albums or a domain error
     */
    suspend fun getTopAlbums(limit: Int = 30): DomainResult<List<Album>>

    /**
     * Get albums filtered by music genre.
     * Uses search API with genre-specific search terms.
     *
     * @param genre The music genre to filter by
     * @param limit Maximum number of albums to fetch (default: 30)
     * @return DomainResult containing list of albums or a domain error
     */
    suspend fun getAlbumsByGenre(
        genre: MusicGenre,
        limit: Int = 30
    ): DomainResult<List<Album>>
}
