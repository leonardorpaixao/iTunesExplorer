package com.itunesexplorer.catalog.data.api

import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.serialization.json.Json

/**
 * Platform-specific factory function to create ITunesApi implementation.
 * iOS uses a custom implementation that manually parses JSON to avoid Content-Length issues.
 */
internal expect fun createITunesApiImpl(
    httpClient: HttpClient,
    baseUrl: String,
    json: Json
): ITunesApi

internal class ITunesApiImpl(
    private val httpClient: HttpClient,
    private val baseUrl: String
) : ITunesApi {

    override suspend fun search(
        term: String,
        media: String?,
        limit: Int,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        return httpClient.get("${baseUrl}search") {
            parameter("term", term)
            media?.let { parameter("media", it) }
            parameter("limit", limit)
            parameter("lang", lang)
            country?.let { parameter("country", it) }
        }.body()
    }

    override suspend fun searchByGenre(
        genre: String,
        limit: Int,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        return httpClient.get("${baseUrl}search") {
            parameter("term", genre)
            parameter("media", "music")
            parameter("entity", "album")
            parameter("limit", limit)
            parameter("lang", lang)
            country?.let { parameter("country", it) }
        }.body<ITunesSearchResponse>()
    }

    override suspend fun details(
        id: String?,
        amgArtistId: String?,
        upc: String?,
        isbn: String?,
        entity: String?,
        limit: Int,
        sort: String,
        lang: String,
        country: String?
    ): ITunesSearchResponse {
        return httpClient.get("${baseUrl}lookup") {
            id?.let { parameter("id", it) }
            amgArtistId?.let { parameter("amgArtistId", it) }
            upc?.let { parameter("upc", it) }
            isbn?.let { parameter("isbn", it) }
            entity?.let { parameter("entity", it) }
            parameter("limit", limit)
            parameter("sort", sort)
            parameter("lang", lang)
            country?.let { parameter("country", it) }
        }.body()
    }

    override suspend fun topAlbums(limit: Int, country: String): ITunesRssResponse {
        return httpClient.get("${baseUrl}${country}/rss/topalbums/limit=${limit}/json").body()
    }
}
