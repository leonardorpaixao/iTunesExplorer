package com.itunesexplorer.catalog.di

import com.itunesexplorer.catalog.data.repository.AlbumsRepositoryImpl
import com.itunesexplorer.catalog.data.repository.DetailsRepositoryImpl
import com.itunesexplorer.catalog.data.repository.SearchRepositoryImpl
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.catalog.shared.data.api.CatalogApi
import com.itunesexplorer.catalog.shared.data.api.CatalogApiImpl
import com.itunesexplorer.catalog.presentation.albums.AlbumsTabModel
import com.itunesexplorer.catalog.presentation.search.SearchTabModel
import com.itunesexplorer.catalog.presentation.details.DetailsScreenModel
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.bindFactory
import org.kodein.di.instance

val catalogModule = DI.Module("catalogModule") {
    // Data layer - APIs
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
    bindSingleton { AlbumsTabModel(instance(), instance()) }
    bindSingleton { SearchTabModel(instance(), CountryManager) }
    bindFactory<String, DetailsScreenModel> { itemId ->
        DetailsScreenModel(instance(), itemId)
    }
}
