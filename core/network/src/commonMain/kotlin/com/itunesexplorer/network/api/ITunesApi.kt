package com.itunesexplorer.network.api

import com.itunesexplorer.network.models.ITunesSearchResponse

interface ITunesApi {

    suspend fun search(
        term: String,
        media: String? = null,
        entity: String? = null,
        attribute: String? = null,
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
        sort: String = "recent"
    ): ITunesSearchResponse
}
