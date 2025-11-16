package com.itunesexplorer.i18n

import java.util.Locale

actual fun getSystemCountry(): String {
    return try {
        Locale.getDefault().country.takeIf { it.isNotEmpty() } ?: "US"
    } catch (e: Exception) {
        println("❌ [SystemCountry] Error detecting country: ${e.message}")
        "US"
    }
}
