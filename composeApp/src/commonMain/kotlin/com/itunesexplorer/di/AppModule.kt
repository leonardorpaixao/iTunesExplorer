package com.itunesexplorer.di

import com.itunesexplorer.home.di.homeModule
import com.itunesexplorer.network.di.networkModule
import org.kodein.di.DI

val appDI = DI {
    importAll(
        networkModule,
        homeModule
    )
}
