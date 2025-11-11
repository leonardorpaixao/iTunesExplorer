package com.itunesexplorer.network.api

import com.itunesexplorer.network.models.ITunesRssResponse
import com.itunesexplorer.network.models.ITunesSearchResponse

interface ITunesApi {

    suspend fun search(
        term: String,
        media: String? = null,
        entity: String? = null,
        attribute: String? = null,
        limit: Int = 50,
        lang: String = "en_us",
        country: String = "US"
    ): ITunesSearchResponse

    suspend fun lookup(
        id: String? = null,
        amgArtistId: String? = null,
        upc: String? = null,
        isbn: String? = null,
        entity: String? = null,
        limit: Int = 50,
        sort: String = "recent"
    ): ITunesSearchResponse

    suspend fun topAlbums(
        limit: Int = 10,
        country: String = "us"
    ): ITunesRssResponse
}
