package com.itunesexplorer.catalog.data.api

import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class CatalogApiImpl(
    private val httpClient: HttpClient
) : CatalogApi {

    private val baseUrl = "https://itunes.apple.com/"

    override suspend fun topAlbums(limit: Int, country: String): ITunesRssResponse {
        return httpClient.get("${baseUrl}${country}/rss/topalbums/limit=${limit}/json").body()
    }
}
