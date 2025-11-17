package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.core.error.runCatchingDomain
import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.data.mapper.SearchResultMapper
import com.itunesexplorer.catalog.domain.model.ItemDetails
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.core.common.domain.DomainError

/**
 * Implementation of DetailsRepository using iTunes Lookup API.
 * Fetches item details and separates main item from related items.
 */
internal class DetailsRepositoryImpl(
    private val api: ITunesApi
) : DetailsRepository {

    override suspend fun getItemDetails(itemId: String): com.itunesexplorer.core.common.domain.DomainResult<ItemDetails> {
        return runCatchingDomain {
            val response = api.details(id = itemId)

            // Convert all items to domain models
            val allItems = SearchResultMapper.toDomainList(response.results)

            // First item is the main item (collection/album)
            val mainItem = allItems.firstOrNull()
                ?: throw IllegalStateException("No items found for ID: $itemId")

            // Remaining items are related items (e.g., tracks)
            val relatedItems = allItems.drop(1)

            // Get store URL from main item
            val storeUrl = mainItem.viewUrl

            ItemDetails(
                mainItem = mainItem,
                relatedItems = relatedItems,
                storeUrl = storeUrl
            )
        }
    }
}
