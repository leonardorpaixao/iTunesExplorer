package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.data.api.ITunesSearchResponse
import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.core.error.DomainResult
import com.itunesexplorer.core.error.runCatchingDomain

/**
 * Implementation of AlbumsRepository.
 * Provides direct access to iTunes API responses without transformation.
 */
internal class AlbumsRepositoryImpl(
    private val iTunesApi: ITunesApi
) : AlbumsRepository {

    override suspend fun getTopAlbumsRss(limit: Int, country: String): DomainResult<ITunesRssResponse> {
        return runCatchingDomain {
            iTunesApi.topAlbums(limit = limit, country = country)
        }
    }

    override suspend fun searchAlbumsByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): DomainResult<ITunesSearchResponse> {
        return runCatchingDomain {
            iTunesApi.searchByGenre(
                genre = genre,
                limit = limit,
                lang = lang,
                country = country
            )
        }
    }
}
