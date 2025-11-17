package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.core.error.runCatchingDomain
import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.data.mapper.SearchResultMapper
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.core.common.domain.DomainResult
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager

/**
 * Implementation of SearchRepository using iTunes Search API.
 * Handles country/language settings and data transformation.
 */
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

            SearchResultMapper.toDomainList(response.results)
        }
    }
}
