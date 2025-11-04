package com.itunesexplorer.di

import com.itunesexplorer.listing.di.listingModule
import com.itunesexplorer.network.di.networkModule
import org.kodein.di.DI

val appDI = DI {
    importAll(
        networkModule,
        listingModule
    )
}
