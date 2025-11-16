package com.itunesexplorer.preferences.di

import com.itunesexplorer.preferences.presentation.PreferencesTabModel
import org.kodein.di.DI
import org.kodein.di.bindProvider

val preferencesModule = DI.Module("preferencesModule") {
    bindProvider { PreferencesTabModel() }
}
