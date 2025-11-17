package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.core.common.domain.DomainResult

/**
 * Repository interface for searching iTunes Store content.
 * Abstracts data source details from the presentation layer.
 */
interface SearchRepository {
    /**
     * Search for items in the iTunes Store.
     *
     * @param query The search term
     * @param mediaType The type of media to search for
     * @param limit Maximum number of results
     * @return DomainResult containing list of search results or a domain error
     */
    suspend fun search(
        query: String,
        mediaType: MediaType,
        limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT
    ): DomainResult<List<SearchResult>>
}
