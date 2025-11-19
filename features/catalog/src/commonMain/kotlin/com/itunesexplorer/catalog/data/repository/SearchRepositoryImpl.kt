package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.core.error.DomainResult
import com.itunesexplorer.foundation.extensions.runCatchingDomain
import com.itunesexplorer.settings.CountryManager
import com.itunesexplorer.settings.LanguageManager

internal class SearchRepositoryImpl(
    private val api: ITunesApi,
    private val countryManager: CountryManager,
    private val languageManager: LanguageManager
) : SearchRepository {

    override suspend fun search(
        query: String,
        mediaType: MediaType,
        limit: Int
    ): DomainResult<List<SearchResult>> {
        return runCatchingDomain {
            val country = countryManager.getCurrentCountryCode()
            val lang = languageManager.getITunesLanguageCode()

            val response = api.search(
                term = query,
                media = if (mediaType == MediaType.ALL) null else mediaType.value,
                limit = limit,
                lang = lang,
                country = country
            )

            response.results.map { value ->
                value.toSearchResult()
            }
        }
    }
}
