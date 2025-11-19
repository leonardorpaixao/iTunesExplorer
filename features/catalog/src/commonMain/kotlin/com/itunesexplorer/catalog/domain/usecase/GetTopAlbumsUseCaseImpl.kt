package com.itunesexplorer.catalog.domain.usecase

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.data.models.RssFeedEntry
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.model.Money
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.core.error.DomainResult
import com.itunesexplorer.settings.CountryManager

interface GetTopAlbumsUseCase {
    suspend operator fun invoke(limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT): DomainResult<List<Album>>
}

internal class GetTopAlbumsUseCaseImpl(
    private val albumsRepository: AlbumsRepository,
    private val countryManager: CountryManager
) : GetTopAlbumsUseCase {
    override suspend operator fun invoke(limit: Int): DomainResult<List<Album>> {
        val country = countryManager.getCurrentCountryCode() ?: CatalogConstants.DEFAULT_COUNTRY_CODE

        return albumsRepository.getTopAlbumsRss(limit, country).map { rssResponse ->
            rssResponse.feed.entry.map { mapRssToAlbum(it) }
        }
    }

    private fun mapRssToAlbum(entry: RssFeedEntry): Album {
        val imageUrl = entry.imImage.lastOrNull()?.label

        val price = entry.imPrice?.let { priceData ->
            val amount = priceData.attributes.amount.toDoubleOrNull()
            val currency = priceData.attributes.currency
            Money.fromOptional(amount, currency)
        }

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
}
