package com.itunesexplorer.settings.data

import com.russhwolf.settings.Settings
import com.russhwolf.settings.set

/**
 * Implementation of PreferencesRepository using multiplatform-settings
 */
class PreferencesRepositoryImpl(
    private val settings: Settings
) : PreferencesRepository {

    companion object {
        private const val KEY_LANGUAGE = "language_preference"
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
}
