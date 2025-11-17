package com.itunesexplorer.i18n

import com.itunesexplorer.core.logger.LogLevel
import com.itunesexplorer.core.logger.createPlatformLogger
import java.util.Locale

private val logger = createPlatformLogger(LogLevel.DEBUG)

actual fun getSystemCountry(): String {
    return try {
        Locale.getDefault().country.takeIf { it.isNotEmpty() } ?: "US"
    } catch (e: Exception) {
        logger.error("SystemCountry", "Error detecting country: ${e.message}", e)
        "US"
    }
}
