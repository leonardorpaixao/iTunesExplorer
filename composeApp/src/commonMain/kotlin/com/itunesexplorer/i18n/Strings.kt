package com.itunesexplorer.i18n

import cafe.adriel.lyricist.LyricistStrings
import com.itunesexplorer.catalog.presentation.i18n.CatalogStrings
import com.itunesexplorer.catalog.presentation.i18n.DeCatalogStrings
import com.itunesexplorer.catalog.presentation.i18n.EnCatalogStrings
import com.itunesexplorer.catalog.presentation.i18n.EsCatalogStrings
import com.itunesexplorer.catalog.presentation.i18n.FrCatalogStrings
import com.itunesexplorer.catalog.presentation.i18n.PtBrCatalogStrings
import com.itunesexplorer.catalog.presentation.i18n.PtPtCatalogStrings
import com.itunesexplorer.home.i18n.DeHomeStrings
import com.itunesexplorer.home.i18n.EnHomeStrings
import com.itunesexplorer.home.i18n.EsHomeStrings
import com.itunesexplorer.home.i18n.FrHomeStrings
import com.itunesexplorer.home.i18n.HomeStrings
import com.itunesexplorer.home.i18n.PtBrHomeStrings
import com.itunesexplorer.home.i18n.PtPtHomeStrings
import com.itunesexplorer.preferences.i18n.DePreferencesStrings
import com.itunesexplorer.preferences.i18n.EnPreferencesStrings
import com.itunesexplorer.preferences.i18n.EsPreferencesStrings
import com.itunesexplorer.preferences.i18n.FrPreferencesStrings
import com.itunesexplorer.preferences.i18n.PreferencesStrings
import com.itunesexplorer.preferences.i18n.PtBrPreferencesStrings
import com.itunesexplorer.preferences.i18n.PtPtPreferencesStrings

data class AppStrings(
    val home: HomeStrings,
    val catalog: CatalogStrings,
    val preferences: PreferencesStrings,
)

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = AppStrings(
    home = EnHomeStrings,
    catalog = EnCatalogStrings,
    preferences = EnPreferencesStrings,
)

@LyricistStrings(languageTag = Locales.PT_BR)
val PtBrStrings = AppStrings(
    home = PtBrHomeStrings,
    catalog = PtBrCatalogStrings,
    preferences = PtBrPreferencesStrings,
)

@LyricistStrings(languageTag = Locales.PT_PT)
val PtPtStrings = AppStrings(
    home = PtPtHomeStrings,
    catalog = PtPtCatalogStrings,
    preferences = PtPtPreferencesStrings,
)

@LyricistStrings(languageTag = Locales.FR)
val FrStrings = AppStrings(
    home = FrHomeStrings,
    catalog = FrCatalogStrings,
    preferences = FrPreferencesStrings,
)

@LyricistStrings(languageTag = Locales.ES)
val EsStrings = AppStrings(
    home = EsHomeStrings,
    catalog = EsCatalogStrings,
    preferences = EsPreferencesStrings,
)

@LyricistStrings(languageTag = Locales.DE)
val DeStrings = AppStrings(
    home = DeHomeStrings,
    catalog = DeCatalogStrings,
    preferences = DePreferencesStrings,
)
