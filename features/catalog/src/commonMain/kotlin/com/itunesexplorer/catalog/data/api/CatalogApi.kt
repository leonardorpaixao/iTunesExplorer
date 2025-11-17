package com.itunesexplorer.catalog.data.api

import com.itunesexplorer.catalog.data.models.ITunesRssResponse

interface CatalogApi {
    suspend fun topAlbums(
        limit: Int = 10,
        country: String = "us"
    ): ITunesRssResponse
}
