package com.itunesexplorer.home.di

import com.itunesexplorer.home.presentation.HomeScreenModel
import com.itunesexplorer.home.presentation.albums.AlbumsTabModel
import com.itunesexplorer.home.presentation.search.SearchTabModel
import com.itunesexplorer.home.presentation.preferences.PreferencesTabModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val homeModule = DI.Module("homeModule") {
    bindProvider { HomeScreenModel() }
    bindProvider { AlbumsTabModel(instance()) }
    bindProvider { SearchTabModel(instance()) }
    bindProvider { PreferencesTabModel() }
}
