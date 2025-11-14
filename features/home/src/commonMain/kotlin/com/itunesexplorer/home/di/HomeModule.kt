package com.itunesexplorer.home.di

import com.itunesexplorer.home.presentation.HomeScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val homeModule = DI.Module("homeModule") {
    bindProvider { HomeScreenModel(instance()) }
}
