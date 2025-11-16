package com.itunesexplorer.i18n

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.languageCode
import platform.Foundation.countryCode

actual fun getSystemLanguage(): String {
    val locale = NSLocale.currentLocale
    val language = locale.languageCode ?: "en"
    val country = locale.countryCode ?: ""

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

actual fun setHtmlLangAttribute(languageTag: String) {
    // No-op on iOS (not a web platform)
}
