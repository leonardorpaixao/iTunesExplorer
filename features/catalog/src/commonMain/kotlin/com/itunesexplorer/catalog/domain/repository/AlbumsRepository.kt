package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.data.models.ITunesSearchResponse
import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import com.itunesexplorer.core.error.DomainResult

interface AlbumsRepository {
    suspend fun getTopAlbumsRss(limit: Int, country: String): DomainResult<ITunesRssResponse>
    suspend fun searchAlbumsByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): DomainResult<ITunesSearchResponse>
}
