package com.itunesexplorer.i18n

import cafe.adriel.lyricist.LyricistStrings
import com.itunesexplorer.home.i18n.DeHomeStrings
import com.itunesexplorer.home.i18n.EnHomeStrings
import com.itunesexplorer.home.i18n.EsHomeStrings
import com.itunesexplorer.home.i18n.FrHomeStrings
import com.itunesexplorer.home.i18n.HomeStrings
import com.itunesexplorer.home.i18n.PtBrHomeStrings
import com.itunesexplorer.home.i18n.PtPtHomeStrings

data class AppStrings(
    val home: HomeStrings,
)

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnStrings = AppStrings(
    home =EnHomeStrings,
)

@LyricistStrings(languageTag = Locales.PT_BR)
val PtBrStrings = AppStrings(
    home =PtBrHomeStrings,
)

@LyricistStrings(languageTag = Locales.PT_PT)
val PtPtStrings = AppStrings(
    home =PtPtHomeStrings,
)

@LyricistStrings(languageTag = Locales.FR)
val FrStrings = AppStrings(
    home =FrHomeStrings,
)

@LyricistStrings(languageTag = Locales.ES)
val EsStrings = AppStrings(
    home =EsHomeStrings,
)

@LyricistStrings(languageTag = Locales.DE)
val DeStrings = AppStrings(
    home =DeHomeStrings,
)
