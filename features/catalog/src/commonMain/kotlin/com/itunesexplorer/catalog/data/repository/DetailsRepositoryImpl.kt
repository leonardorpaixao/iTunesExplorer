package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.core.error.runCatchingDomain
import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.domain.model.ItemDetails
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager

/**
 * Implementation of DetailsRepository using iTunes Lookup API.
 * Fetches item details and separates main item from related items.
 * Handles country/language settings.
 */
internal class DetailsRepositoryImpl(
    private val api: ITunesApi,
    private val countryManager: CountryManager,
    private val languageManager: LanguageManager
) : DetailsRepository {

    override suspend fun getItemDetails(itemId: String): com.itunesexplorer.core.common.domain.DomainResult<ItemDetails> {
        return runCatchingDomain {
            val country = countryManager.getCurrentCountryCode()
            val lang = languageManager.getITunesLanguageCode()

            val response = api.details(
                id = itemId,
                lang = lang,
                country = country
            )

            val allItems = response.results.map {
                it.toSearchResult()
            }

            val mainItem = allItems.firstOrNull()
                ?: throw IllegalStateException("No items found for ID: $itemId")

            val relatedItems = allItems.drop(1)

            ItemDetails(
                mainItem = mainItem,
                relatedItems = relatedItems,
            )
        }
    }
}
