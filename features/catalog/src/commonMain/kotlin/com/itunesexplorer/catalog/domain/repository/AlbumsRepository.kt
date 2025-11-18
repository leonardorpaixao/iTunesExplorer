package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.data.api.ITunesSearchResponse
import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import com.itunesexplorer.core.error.DomainResult

/**
 * Repository interface for accessing raw iTunes API data.
 * Returns unprocessed API responses for use cases to transform.
 */
interface AlbumsRepository {
    suspend fun getTopAlbumsRss(limit: Int, country: String): DomainResult<ITunesRssResponse>
    suspend fun searchAlbumsByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): DomainResult<ITunesSearchResponse>
}
