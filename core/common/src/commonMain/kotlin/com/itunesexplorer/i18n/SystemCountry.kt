package com.itunesexplorer.i18n

/**
 * Gets the system country code (ISO 3166-1 alpha-2)
 * Returns the country code of the device's current locale
 * Defaults to "US" if unable to determine
 */
expect fun getSystemCountry(): String
