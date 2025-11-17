package com.itunesexplorer.catalog.data.api

import io.ktor.client.*
import kotlinx.serialization.json.Json

internal actual fun createITunesApiImpl(
    httpClient: HttpClient,
    baseUrl: String,
    json: Json
): ITunesApi {
    return ITunesApiImpl(httpClient, baseUrl)
}
