package com.itunesexplorer.foundation.i18n

import com.itunesexplorer.core.logger.LogLevel
import com.itunesexplorer.core.logger.createPlatformLogger
import platform.Foundation.NSLocale
import platform.Foundation.currentLocale
import platform.Foundation.countryCode

private val logger = createPlatformLogger(LogLevel.DEBUG)

actual fun getSystemCountry(): String {
    return try {
        (NSLocale.currentLocale.countryCode as? String)?.takeIf { it.isNotEmpty() } ?: "US"
    } catch (e: Exception) {
        logger.error("SystemCountry", "Error detecting country: ${e.message}", e)
        "US"
    }
}
