package com.itunesexplorer.catalog.di

import com.itunesexplorer.catalog.shared.data.api.CatalogApi
import com.itunesexplorer.catalog.shared.data.api.CatalogApiImpl
import com.itunesexplorer.catalog.presentation.albums.AlbumsTabModel
import com.itunesexplorer.catalog.presentation.search.SearchTabModel
import com.itunesexplorer.catalog.presentation.details.DetailsScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.bindFactory
import org.kodein.di.instance

val catalogModule = DI.Module("catalogModule") {
    // Data layer
    bindSingleton<CatalogApi> { CatalogApiImpl(instance()) }

    // Presentation layer
    bindSingleton { AlbumsTabModel(instance(), instance()) }
    bindSingleton { SearchTabModel(instance()) }
    bindFactory<String, DetailsScreenModel> { itemId ->
        DetailsScreenModel(instance(), itemId)
    }
}
