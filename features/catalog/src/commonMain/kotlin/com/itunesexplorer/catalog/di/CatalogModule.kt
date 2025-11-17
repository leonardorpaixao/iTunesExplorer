package com.itunesexplorer.catalog.di

import com.itunesexplorer.catalog.data.repository.AlbumsRepositoryImpl
import com.itunesexplorer.catalog.data.repository.DetailsRepositoryImpl
import com.itunesexplorer.catalog.data.repository.SearchRepositoryImpl
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.catalog.data.api.CatalogApi
import com.itunesexplorer.catalog.data.api.CatalogApiImpl
import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.data.api.ITunesApiImpl
import com.itunesexplorer.catalog.presentation.albums.AlbumsTabModel
import com.itunesexplorer.catalog.presentation.search.SearchTabModel
import com.itunesexplorer.catalog.presentation.details.DetailsScreenModel
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager
import io.ktor.client.*
import io.ktor.client.plugins.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.logging.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.serialization.json.Json
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindFactory
import org.kodein.di.instance

private const val BASE_URL = "https://itunes.apple.com/"

val catalogModule = DI.Module("catalogModule") {
    // Network layer
    bindSingleton { createJson() }
    bindSingleton { createHttpClient(instance()) }

    // Data layer - APIs
    bindSingleton<ITunesApi> { createITunesApi(instance()) }
    bindSingleton<CatalogApi> { CatalogApiImpl(instance()) }

    // Data layer - Repositories
    bindSingleton<SearchRepository> {
        SearchRepositoryImpl(
            api = instance(),
            countryManager = CountryManager,
            languageManager = LanguageManager
        )
    }

    bindSingleton<AlbumsRepository> {
        AlbumsRepositoryImpl(
            catalogApi = instance(),
            iTunesApi = instance(),
            countryManager = CountryManager,
            languageManager = LanguageManager
        )
    }

    bindSingleton<DetailsRepository> {
        DetailsRepositoryImpl(api = instance())
    }

    // Presentation layer
    bindSingleton { AlbumsTabModel(instance(), CountryManager) }
    bindSingleton { SearchTabModel(instance(), CountryManager) }
    bindFactory<String, DetailsScreenModel> { itemId ->
        DetailsScreenModel(instance(), itemId)
    }
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

private fun createITunesApi(client: HttpClient): ITunesApi {
    return ITunesApiImpl(client, BASE_URL)
}
