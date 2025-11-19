package com.itunesexplorer.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton manager for handling country/region changes across the app
 */
object CountryManager {
    private val _currentCountry = MutableStateFlow<String?>(null)

    val currentCountry: StateFlow<String?> = _currentCountry.asStateFlow()

    fun setCountry(countryCode: String) {
        _currentCountry.value = countryCode
    }

    fun initialize(countryCode: String) {
        if (_currentCountry.value == null) {
            _currentCountry.value = countryCode
        }
    }

    fun getCurrentCountryCode(): String? = _currentCountry.value

    fun clear() {
        _currentCountry.value = null
    }
}