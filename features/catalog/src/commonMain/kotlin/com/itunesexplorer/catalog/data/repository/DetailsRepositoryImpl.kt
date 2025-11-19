package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.core.error.DomainResult
import com.itunesexplorer.foundation.extensions.runCatchingDomain
import com.itunesexplorer.settings.CountryManager
import com.itunesexplorer.settings.LanguageManager

internal class DetailsRepositoryImpl(
    private val api: ITunesApi,
    private val countryManager: CountryManager,
    private val languageManager: LanguageManager
) : DetailsRepository {

    override suspend fun getItemDetails(itemId: String): DomainResult<SearchResult> {
        return runCatchingDomain {
            val country = countryManager.getCurrentCountryCode()
            val lang = languageManager.getITunesLanguageCode()

            val response = api.details(
                id = itemId,
                lang = lang,
                country = country
            )

            val details = response.results.firstOrNull()?.toSearchResult()
                ?: throw IllegalStateException("No items found for ID: $itemId")

            details
        }
    }
}
