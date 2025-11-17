package com.itunesexplorer.di

import com.itunesexplorer.core.logger.di.loggerModule
import com.itunesexplorer.currency.di.currencyModule
import com.itunesexplorer.home.di.homeModule
import com.itunesexplorer.settings.di.settingsModule
import org.kodein.di.DI

val appDI = DI {
    importAll(
        loggerModule,
        settingsModule,
        currencyModule,
        homeModule
        // preferencesModule is imported by homeModule
        // catalogModule (with network setup) is imported by homeModule
    )
}
