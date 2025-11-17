package com.itunesexplorer.catalog.domain.usecase

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.data.api.ITunesItem
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.model.MusicGenre
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.core.common.domain.DomainResult
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager

/**
 * Use case for fetching albums filtered by genre from the iTunes Search API.
 * Handles country/language configuration and transforms search results to domain models.
 * Note: RSS feed doesn't support genre filtering, so this uses the Search API.
 */
class GetAlbumsByGenreUseCase(
    private val albumsRepository: AlbumsRepository,
    private val countryManager: CountryManager,
    private val languageManager: LanguageManager
) {
    suspend operator fun invoke(
        genre: MusicGenre,
        limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT
    ): DomainResult<List<Album>> {
        val country = countryManager.getCurrentCountryCode()
        val lang = languageManager.getITunesLanguageCode()

        return albumsRepository.searchAlbumsByGenre(
            genre = genre.searchTerm,
            limit = limit,
            lang = lang,
            country = country
        ).map { searchResponse ->
            searchResponse.results.mapNotNull { mapITunesItemToAlbum(it) }
        }
    }

    private fun mapITunesItemToAlbum(item: ITunesItem): Album? {
        val collectionId = item.collectionId ?: return null
        val collectionName = item.collectionName ?: return null

        val imageUrl = item.artworkUrl100 ?: item.artworkUrl60 ?: item.artworkUrl30

        val price = Money.fromOptional(item.collectionPrice, item.currency)

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
}
