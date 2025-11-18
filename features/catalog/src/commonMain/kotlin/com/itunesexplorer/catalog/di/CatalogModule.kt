package com.itunesexplorer.catalog.di

import com.itunesexplorer.catalog.data.repository.AlbumsRepositoryImpl
import com.itunesexplorer.catalog.data.repository.DetailsRepositoryImpl
import com.itunesexplorer.catalog.data.repository.SearchRepositoryImpl
import com.itunesexplorer.catalog.domain.repository.AlbumsRepository
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.catalog.domain.usecase.GetAlbumsByGenreUseCase
import com.itunesexplorer.catalog.domain.usecase.GetAlbumsByGenreUseCaseImpl
import com.itunesexplorer.catalog.domain.usecase.GetTopAlbumsUseCase
import com.itunesexplorer.catalog.domain.usecase.GetTopAlbumsUseCaseImpl
import com.itunesexplorer.catalog.data.api.ITunesApi
import com.itunesexplorer.catalog.data.api.createITunesApiImpl
import com.itunesexplorer.catalog.presentation.albums.AlbumsTabModel
import com.itunesexplorer.catalog.presentation.search.SearchTabModel
import com.itunesexplorer.catalog.presentation.details.DetailsScreenModel
import com.itunesexplorer.core.network.di.networkModule
import com.itunesexplorer.settings.country.CountryManager
import com.itunesexplorer.settings.language.LanguageManager
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindFactory
import org.kodein.di.instance

private const val BASE_URL = "https://itunes.apple.com/"

val catalogModule = DI.Module("catalogModule") {
    importOnce(networkModule)

    bindSingleton<ITunesApi> {
        createITunesApiImpl(
            httpClient = instance(),
            baseUrl = BASE_URL,
            json = instance()
        )
    }

    bindSingleton<SearchRepository> {
        SearchRepositoryImpl(
            api = instance(),
            countryManager = CountryManager,
            languageManager = LanguageManager
        )
    }

    bindSingleton<AlbumsRepository> {
        AlbumsRepositoryImpl(iTunesApi = instance())
    }

    bindSingleton<DetailsRepository> {
        DetailsRepositoryImpl(
            api = instance(),
            countryManager = CountryManager,
            languageManager = LanguageManager
        )
    }

    bindSingleton<GetTopAlbumsUseCase> {
        GetTopAlbumsUseCaseImpl(
            albumsRepository = instance(),
            countryManager = CountryManager
        )
    }

    bindSingleton<GetAlbumsByGenreUseCase> {
        GetAlbumsByGenreUseCaseImpl(
            albumsRepository = instance(),
            countryManager = CountryManager,
            languageManager = LanguageManager
        )
    }

    bindSingleton {
        AlbumsTabModel(
            getTopAlbumsUseCase = instance(),
            getAlbumsByGenreUseCase = instance(),
            countryManager = CountryManager
        )
    }
    bindSingleton { SearchTabModel(instance(), CountryManager) }
    bindFactory<String, DetailsScreenModel> { itemId ->
        DetailsScreenModel(instance(), itemId)
    }
}
