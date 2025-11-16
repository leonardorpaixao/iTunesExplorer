package com.itunesexplorer.catalog.shared.data.api

import com.itunesexplorer.catalog.shared.data.models.ITunesRssResponse

interface CatalogApi {
    suspend fun topAlbums(
        limit: Int = 10,
        country: String = "us"
    ): ITunesRssResponse
}
