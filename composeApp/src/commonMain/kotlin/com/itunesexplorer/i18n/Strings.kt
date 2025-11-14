package com.itunesexplorer.i18n

import cafe.adriel.lyricist.LyricistStrings
import com.itunesexplorer.listing.i18n.DeListingStrings
import com.itunesexplorer.listing.i18n.EnListingStrings
import com.itunesexplorer.listing.i18n.EsListingStrings
import com.itunesexplorer.listing.i18n.FrListingStrings
import com.itunesexplorer.listing.i18n.ListingStrings
import com.itunesexplorer.listing.i18n.PtBrListingStrings
import com.itunesexplorer.listing.i18n.PtPtListingStrings

data class AppStrings(
    val listing: ListingStrings,
)

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = AppStrings(
    listing = EnListingStrings,
)

@LyricistStrings(languageTag = Locales.PT_BR)
val PtBrStrings = AppStrings(
    listing = PtBrListingStrings,
)

@LyricistStrings(languageTag = Locales.PT_PT)
val PtPtStrings = AppStrings(
    listing = PtPtListingStrings,
)

@LyricistStrings(languageTag = Locales.FR)
val FrStrings = AppStrings(
    listing = FrListingStrings,
)

@LyricistStrings(languageTag = Locales.ES)
val EsStrings = AppStrings(
    listing = EsListingStrings,
)

@LyricistStrings(languageTag = Locales.DE)
val DeStrings = AppStrings(
    listing = DeListingStrings,
)
