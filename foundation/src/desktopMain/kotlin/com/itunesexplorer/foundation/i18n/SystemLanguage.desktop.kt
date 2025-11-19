package com.itunesexplorer.foundation.i18n

import com.itunesexplorer.foundation.i18n.Locales
import java.util.Locale

actual fun getSystemLanguage(): String {
    val locale = Locale.getDefault()
    val language = locale.language
    val country = locale.country

    // Map system locale to our supported locales
    return when {
        language == "pt" && country == "BR" -> Locales.PT_BR
        language == "pt" && country == "PT" -> Locales.PT_PT
        language == "pt" -> Locales.PT_BR // Default Portuguese to Brazil
        language == "fr" -> Locales.FR
        language == "es" -> Locales.ES
        language == "de" -> Locales.DE
        else -> Locales.EN
    }
}
