package com.itunesexplorer.settings

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

interface PreferencesRepository {
    suspend fun getLanguage(): String?
    suspend fun setLanguage(languageTag: String)
    suspend fun clearLanguage()
    suspend fun getCountry(): String?
    suspend fun setCountry(countryCode: String)
    suspend fun clearCountry()
}

internal class PreferencesRepositoryImpl(
    private val settings: Settings
) : PreferencesRepository {

    companion object {
        private const val KEY_LANGUAGE = "language_preference"
        private const val KEY_COUNTRY = "country_preference"
    }

    override suspend fun getLanguage(): String? {
        return settings.getStringOrNull(KEY_LANGUAGE)
    }

    override suspend fun setLanguage(languageTag: String) {
        settings[KEY_LANGUAGE] = languageTag
    }

    override suspend fun clearLanguage() {
        settings.remove(KEY_LANGUAGE)
    }

    override suspend fun getCountry(): String? {
        return settings.getStringOrNull(KEY_COUNTRY)
    }

    override suspend fun setCountry(countryCode: String) {
        settings[KEY_COUNTRY] = countryCode
    }

    override suspend fun clearCountry() {
        settings.remove(KEY_COUNTRY)
    }
}
