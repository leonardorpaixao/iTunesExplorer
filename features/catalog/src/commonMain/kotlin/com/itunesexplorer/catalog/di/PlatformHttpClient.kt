package com.itunesexplorer.catalog.di

import io.ktor.client.*
import kotlinx.serialization.json.Json

/**
 * Platform-specific HTTP client configuration.
 * iOS (Darwin engine) requires special handling for Content-Length validation issues.
 */
internal expect fun createPlatformHttpClient(json: Json): HttpClient
