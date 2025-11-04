package com.itunesexplorer.network.di

import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.api.ITunesApiImpl
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

private const val BASE_URL = "https://itunes.apple.com/"

val networkModule = DI.Module("networkModule") {
    bindSingleton { createJson() }
    bindSingleton { createHttpClient(instance()) }
    bindSingleton<ITunesApi> { createITunesApi(instance()) }
}

private fun createJson(): Json = Json {
    prettyPrint = true
    isLenient = true
    ignoreUnknownKeys = true
    coerceInputValues = true
}

private fun createHttpClient(json: Json): HttpClient {
    return HttpClient {
        install(ContentNegotiation) {
            json(json)
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

private fun createITunesApi(client: HttpClient): ITunesApi {
    return ITunesApiImpl(client, BASE_URL)
}
