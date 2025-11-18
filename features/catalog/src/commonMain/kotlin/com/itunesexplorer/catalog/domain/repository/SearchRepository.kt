package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.core.error.DomainResult

interface SearchRepository {
    suspend fun search(
        query: String,
        mediaType: MediaType,
        limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT
    ): DomainResult<List<SearchResult>>
}
