package com.itunesexplorer.network.api

import com.itunesexplorer.network.models.ITunesRssResponse
import com.itunesexplorer.network.models.ITunesSearchResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*

class ITunesApiImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : ITunesApi {

    override suspend fun search(
        term: String,
        media: String?,
        entity: String?,
        attribute: String?,
        limit: Int,
        lang: String,
        country: String
    ): ITunesSearchResponse {
        return httpClient.get("${baseUrl}search") {
            parameter("term", term)
            media?.let { parameter("media", it) }
            entity?.let { parameter("entity", it) }
            attribute?.let { parameter("attribute", it) }
            parameter("limit", limit)
            parameter("lang", lang)
            parameter("country", country)
        }.body()
    }

    override suspend fun lookup(
        id: String?,
        amgArtistId: String?,
        upc: String?,
        isbn: String?,
        entity: String?,
        limit: Int,
        sort: String
    ): ITunesSearchResponse {
        return httpClient.get("${baseUrl}lookup") {
            id?.let { parameter("id", it) }
            amgArtistId?.let { parameter("amgArtistId", it) }
            upc?.let { parameter("upc", it) }
            isbn?.let { parameter("isbn", it) }
            entity?.let { parameter("entity", it) }
            parameter("limit", limit)
            parameter("sort", sort)
        }.body()
    }

    override suspend fun topAlbums(limit: Int, country: String): ITunesRssResponse {
        return httpClient.get("${baseUrl}${country}/rss/topalbums/limit=${limit}/json").body()
    }
}
