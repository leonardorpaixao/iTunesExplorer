package com.itunesexplorer.i18n

import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.countryCode

actual fun getSystemCountry(): String {
    return try {
        (NSLocale.currentLocale.countryCode as? String)?.takeIf { it.isNotEmpty() } ?: "US"
    } catch (e: Exception) {
        println("❌ [SystemCountry] Error detecting country: ${e.message}")
        "US"
    }
}
