package com.itunesexplorer.network.api

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
        country: String?
    ): ITunesSearchResponse {
        return httpClient.get("${baseUrl}search") {
            parameter("term", term)
            media?.let { parameter("media", it) }
            entity?.let { parameter("entity", it) }
            attribute?.let { parameter("attribute", it) }
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
        val response = httpClient.get("${baseUrl}search") {
            parameter("term", genre)
            parameter("media", "music")
            parameter("entity", "album")
            parameter("limit", limit)
            parameter("lang", lang)
            country?.let { parameter("country", it) }
        }.body<ITunesSearchResponse>()

        // Filter results client-side by primaryGenreName to ensure accuracy
        // Only filter if genre is not "top albums" (which means ALL)
        val filteredResults = if (genre == "top albums") {
            response.results
        } else {
            response.results.filter { item ->
                item.primaryGenreName?.contains(genre, ignoreCase = true) == true
            }
        }

        return response.copy(
            results = filteredResults,
            resultCount = filteredResults.size
        )
    }

    override suspend fun details(
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
}
