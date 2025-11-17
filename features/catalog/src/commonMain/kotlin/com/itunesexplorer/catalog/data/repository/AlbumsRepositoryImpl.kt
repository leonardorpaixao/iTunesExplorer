package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.catalog.data.mapper.AlbumMapper
import com.itunesexplorer.catalog.data.mapper.ErrorMapper
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.catalog.shared.data.api.CatalogApi
import com.itunesexplorer.core.common.domain.DomainError
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.MusicGenre
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager

/**
 * Implementation of AlbumsRepository.
 * Coordinates between RSS feed API (for top albums) and Search API (for genre filtering).
 */
class AlbumsRepositoryImpl(
    private val catalogApi: CatalogApi,
    private val iTunesApi: ITunesApi,
    private val countryManager: CountryManager,
    private val languageManager: LanguageManager
) : AlbumsRepository {

    override suspend fun getTopAlbums(limit: Int): com.itunesexplorer.core.common.domain.DomainResult<List<Album>> {
        return ErrorMapper.execute("Fetch top albums") {
            val country = countryManager.getCurrentCountryCode() ?: "us"

            // Use RSS feed for top albums
            val response = catalogApi.topAlbums(
                limit = limit,
                country = country
            )

            AlbumMapper.fromRssList(response.feed.entry)
        }
    }

    override suspend fun getAlbumsByGenre(
        genre: MusicGenre,
        limit: Int
    ): com.itunesexplorer.core.common.domain.DomainResult<List<Album>> {
        return ErrorMapper.execute("Fetch albums for genre ${genre.genreName}") {
            val country = countryManager.getCurrentCountryCode()
            val lang = languageManager.getITunesLanguageCode()

            // Use Search API for genre-specific results
            // Note: RSS feed doesn't support genre filtering
            val response = iTunesApi.searchByGenre(
                genre = genre.searchTerm,
                limit = limit,
                lang = lang,
                country = country
            )

            AlbumMapper.fromITunesItemList(response.results)
        }
    }
}
