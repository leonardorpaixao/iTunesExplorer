package com.itunesexplorer.preferences.di

import com.itunesexplorer.preferences.presentation.PreferencesTabModel
import com.itunesexplorer.settings.data.PreferencesRepository
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val preferencesModule = DI.Module("preferencesModule") {
    bindProvider {
        PreferencesTabModel(
            preferencesRepository = instance<PreferencesRepository>()
        )
    }
}
