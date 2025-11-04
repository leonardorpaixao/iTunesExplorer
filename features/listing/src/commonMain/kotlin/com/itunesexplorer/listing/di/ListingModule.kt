package com.itunesexplorer.listing.di

import com.itunesexplorer.listing.presentation.ListingScreenModel
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.instance

val listingModule = DI.Module("listingModule") {
    bindProvider { ListingScreenModel(instance()) }
}
