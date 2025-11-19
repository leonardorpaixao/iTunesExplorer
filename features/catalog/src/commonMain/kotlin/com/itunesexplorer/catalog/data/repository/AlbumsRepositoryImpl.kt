package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.data.models.ITunesSearchResponse
import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.core.error.DomainResult
import com.itunesexplorer.foundation.extensions.runCatchingDomain

internal class AlbumsRepositoryImpl(
    private val api: ITunesApi
) : AlbumsRepository {

    override suspend fun getTopAlbumsRss(limit: Int, country: String): DomainResult<ITunesRssResponse> {
        return runCatchingDomain {
            api.topAlbums(limit = limit, country = country)
        }
    }

    override suspend fun searchAlbumsByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): DomainResult<ITunesSearchResponse> {
        return runCatchingDomain {
            api.searchByGenre(
                genre = genre,
                limit = limit,
                lang = lang,
                country = country
            )
        }
    }
}
