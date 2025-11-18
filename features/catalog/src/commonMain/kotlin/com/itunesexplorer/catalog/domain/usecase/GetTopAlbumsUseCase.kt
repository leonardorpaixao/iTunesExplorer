package com.itunesexplorer.catalog.domain.usecase

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.core.error.DomainResult

/**
 * Use case interface for fetching top albums from the iTunes RSS feed.
 */
interface GetTopAlbumsUseCase {
    /**
     * Fetches top albums for the user's current country.
     *
     * @param limit Maximum number of albums to fetch
     * @return DomainResult containing list of albums or a domain error
     */
    suspend operator fun invoke(limit: Int = CatalogConstants.REQUEST_ITEMS_LIMIT): DomainResult<List<Album>>
}
