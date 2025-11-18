package com.itunesexplorer.core.network

import com.itunesexplorer.core.logger.LogLevel as AppLogLevel
import com.itunesexplorer.core.logger.Logger as AppLogger
import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import io.ktor.client.plugins.logging.LogLevel as KtorLogLevel
import io.ktor.client.plugins.logging.Logger as KtorLogger

/**
 * iOS implementation of the HTTP client using Darwin engine.
 *
 * Features:
 * - Darwin engine with cellular access enabled
 * - Content-Length workaround: Sets Accept-Encoding to "identity" to avoid validation issues
 * - JSON content negotiation
 * - Configurable logging based on LogLevel
 * - Request timeout configuration (10s)
 *
 * Note: The DefaultRequest plugin clears headers and sets Accept-Encoding to "identity"
 * to work around Content-Length validation issues specific to the Darwin engine.
 */
actual fun createPlatformHttpClient(json: Json, logger: AppLogger): HttpClient {
    val appLogLevel = logger.logLevel

    return HttpClient(Darwin) {
        engine {
            configureRequest {
                setAllowsCellularAccess(true)
            }
        }

        install(DefaultRequest) {
            headers.clear()
            headers.append(HttpHeaders.AcceptEncoding, "identity")
        }

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
            requestTimeoutMillis = 10_000
            connectTimeoutMillis = 10_000
            socketTimeoutMillis = 10_000
        }
    }
}
