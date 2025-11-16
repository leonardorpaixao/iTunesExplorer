package com.itunesexplorer.settings.country

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton manager for handling country/region changes across the app
 */
object CountryManager {
    private val _currentCountry = MutableStateFlow<String?>(null)

    /**
     * StateFlow emitting the current country code (ISO 3166-1 alpha-2)
     * Null means not yet initialized
     */
    val currentCountry: StateFlow<String?> = _currentCountry.asStateFlow()

    /**
     * Updates the current country and triggers API calls with new region
     * @param countryCode The new country code (e.g., "US", "BR")
     */
    fun setCountry(countryCode: String) {
        _currentCountry.value = countryCode
    }

    /**
     * Initializes the country manager with the given country code
     * Only sets the value if not already initialized
     * @param countryCode The initial country code
     */
    fun initialize(countryCode: String) {
        if (_currentCountry.value == null) {
            _currentCountry.value = countryCode
        }
    }

    /**
     * Gets the current country code or null if not set
     */
    fun getCurrentCountryCode(): String? = _currentCountry.value

    /**
     * Clears the current country selection
     */
    fun clear() {
        _currentCountry.value = null
    }
}
