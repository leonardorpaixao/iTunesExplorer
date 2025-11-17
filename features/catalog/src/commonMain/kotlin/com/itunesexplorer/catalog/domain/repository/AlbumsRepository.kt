package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.data.api.ITunesSearchResponse
import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import com.itunesexplorer.core.common.domain.DomainResult

/**
 * Repository interface for accessing raw iTunes API data.
 * Returns unprocessed API responses for use cases to transform.
 */
interface AlbumsRepository {
    /**
     * Fetch top albums RSS feed for a specific country.
     *
     * @param limit Maximum number of albums to fetch
     * @param country Country code (e.g., "us", "br")
     * @return DomainResult containing raw RSS response or a domain error
     */
    suspend fun getTopAlbumsRss(limit: Int, country: String): DomainResult<ITunesRssResponse>

    /**
     * Search for albums by genre using the iTunes Search API.
     *
     * @param genre Genre search term
     * @param limit Maximum number of results
     * @param lang Language code (e.g., "en_us")
     * @param country Country code (optional)
     * @return DomainResult containing raw search response or a domain error
     */
    suspend fun searchAlbumsByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): DomainResult<ITunesSearchResponse>
}
