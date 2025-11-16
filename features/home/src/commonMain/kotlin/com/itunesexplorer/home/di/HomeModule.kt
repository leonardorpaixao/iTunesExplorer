package com.itunesexplorer.home.di

import com.itunesexplorer.home.presentation.HomeScreenModel
import com.itunesexplorer.catalog.di.catalogModule
import com.itunesexplorer.preferences.di.preferencesModule
import org.kodein.di.DI
import org.kodein.di.bindProvider

val homeModule = DI.Module("homeModule") {
    importAll(catalogModule, preferencesModule)
    bindProvider { HomeScreenModel() }
}
