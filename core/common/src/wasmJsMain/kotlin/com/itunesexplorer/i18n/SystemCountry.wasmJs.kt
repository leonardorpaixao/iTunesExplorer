package com.itunesexplorer.i18n

actual fun getSystemCountry(): String {
    return try {
        // Try to get country from browser navigator.language
        // Format is usually "en-US", "pt-BR", etc
        val language = js("navigator.language") as? String
        language?.split("-")?.getOrNull(1)?.uppercase()?.takeIf { it.length == 2 } ?: "US"
    } catch (e: Exception) {
        println("❌ [SystemCountry] Error detecting country: ${e.message}")
        "US"
    }
}
