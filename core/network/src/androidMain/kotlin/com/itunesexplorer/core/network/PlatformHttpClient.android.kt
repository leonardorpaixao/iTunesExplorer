package com.itunesexplorer.core.network

import com.itunesexplorer.core.logger.LogLevel as AppLogLevel
import com.itunesexplorer.core.logger.Logger as AppLogger
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.plugins.HttpTimeout
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger

/**
 * Android implementation of the HTTP client using OkHttp engine.
 *
 * Features:
 * - JSON content negotiation
 * - Configurable logging based on LogLevel
 * - Request timeout configuration (30s)
 *
 * Note: OkHttp handles content encoding (gzip, deflate) automatically.
 */
actual fun createPlatformHttpClient(json: Json, logger: AppLogger): HttpClient {
    val appLogLevel = logger.logLevel

    return HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(json, contentType = ContentType.Application.Json)
            json(json, contentType = ContentType.Text.JavaScript)
        }

        if (appLogLevel != AppLogLevel.NONE) {
            install(Logging) {
                this.logger = KtorLogger.DEFAULT
                this.level = when (appLogLevel) {
                    AppLogLevel.DEBUG -> KtorLogLevel.ALL
                    AppLogLevel.INFO -> KtorLogLevel.HEADERS
                    AppLogLevel.WARNING, AppLogLevel.ERROR -> KtorLogLevel.HEADERS
                    AppLogLevel.NONE -> KtorLogLevel.NONE
                }
            }
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
    }
}
