package com.itunesexplorer.settings.data

/**
 * Repository for managing app preferences/settings
 */
interface PreferencesRepository {
    /**
     * Gets the saved language preference
     * @return Language tag (e.g., "en", "pt-BR") or null if not set
     */
    suspend fun getLanguage(): String?

    /**
     * Saves the language preference
     * @param languageTag Language tag to save (e.g., "en", "pt-BR")
     */
    suspend fun setLanguage(languageTag: String)

    /**
     * Clears the language preference
     */
    suspend fun clearLanguage()
}
