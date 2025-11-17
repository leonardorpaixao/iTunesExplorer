package com.itunesexplorer.catalog.di

import io.ktor.client.*
import io.ktor.client.engine.darwin.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json

internal actual fun createPlatformHttpClient(json: Json): HttpClient {
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

        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.HEADERS
        }

        install(HttpTimeout) {
            requestTimeoutMillis = 30_000
            connectTimeoutMillis = 30_000
            socketTimeoutMillis = 30_000
        }
    }
}
