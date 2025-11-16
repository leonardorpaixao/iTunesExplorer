package com.itunesexplorer.di

import com.itunesexplorer.home.di.homeModule
import com.itunesexplorer.network.di.networkModule
import com.itunesexplorer.settings.di.settingsModule
import org.kodein.di.DI

val appDI = DI {
    importAll(
        networkModule,
        settingsModule,
        homeModule
        // preferencesModule is imported by homeModule
    )
}
