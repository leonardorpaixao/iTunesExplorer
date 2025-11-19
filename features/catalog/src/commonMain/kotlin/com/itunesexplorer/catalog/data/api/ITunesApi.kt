package com.itunesexplorer.catalog.data.api

import com.itunesexplorer.catalog.data.models.ITunesRssResponse
import com.itunesexplorer.catalog.data.models.ITunesSearchResponse

internal interface ITunesApi {

    suspend fun search(
        term: String,
        media: String? = null,
        limit: Int = 50,
        lang: String = "en_us",
        country: String? = null
    ): ITunesSearchResponse

    suspend fun searchByGenre(
        genre: String,
        limit: Int = 50,
        lang: String = "en_us",
        country: String? = null
    ): ITunesSearchResponse

    suspend fun details(
        id: String? = null,
        amgArtistId: String? = null,
        upc: String? = null,
        isbn: String? = null,
        entity: String? = null,
        limit: Int = 50,
        sort: String = "recent",
        lang: String = "en_us",
        country: String? = null
    ): ITunesSearchResponse

    suspend fun topAlbums(
        limit: Int = 10,
        country: String = "us"
    ): ITunesRssResponse
}
