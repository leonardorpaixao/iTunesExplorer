package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.domain.model.ItemDetails
import com.itunesexplorer.core.common.domain.DomainResult

/**
 * Repository interface for fetching detailed information about iTunes Store items.
 */
interface DetailsRepository {
    /**
     * Get detailed information about a specific item.
     * Separates the main item from related items (e.g., album and its tracks).
     *
     * @param itemId The iTunes Store item ID
     * @return DomainResult containing item details or a domain error
     */
    suspend fun getItemDetails(itemId: String): DomainResult<ItemDetails>
}
