package com.itunesexplorer.catalog.data.repository

import com.itunesexplorer.core.error.runCatchingDomain
import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.domain.model.ItemDetails
import com.itunesexplorer.catalog.domain.repository.DetailsRepository

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

            val allItems = response.results.map {
                it.toDomain()
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
