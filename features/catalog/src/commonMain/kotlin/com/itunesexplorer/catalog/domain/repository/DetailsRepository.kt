package com.itunesexplorer.catalog.domain.repository

import com.itunesexplorer.catalog.domain.model.ItemDetails
import com.itunesexplorer.core.error.DomainResult

/**
 * Repository interface for fetching detailed information about iTunes Store items.
 */
interface DetailsRepository {
    suspend fun getItemDetails(itemId: String): DomainResult<ItemDetails>
}
