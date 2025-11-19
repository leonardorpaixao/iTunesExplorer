package com.itunesexplorer.settings.di

import com.itunesexplorer.settings.PreferencesRepository
import com.itunesexplorer.settings.PreferencesRepositoryImpl
import com.russhwolf.settings.Settings
import org.kodein.di.DI
import org.kodein.di.bind
import org.kodein.di.instance
import org.kodein.di.singleton

val settingsModule = DI.Module("settingsModule") {
    bind<Settings>() with singleton { Settings() }
    bind<PreferencesRepository>() with singleton {
        PreferencesRepositoryImpl(settings = instance())
    }
}
