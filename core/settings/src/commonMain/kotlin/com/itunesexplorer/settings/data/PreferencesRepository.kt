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

    /**
     * Gets the saved country preference
     * @return Country code (e.g., "US", "BR") or null if not set
     */
    suspend fun getCountry(): String?

    /**
     * Saves the country preference
     * @param countryCode Country code to save (e.g., "US", "BR")
     */
    suspend fun setCountry(countryCode: String)

    /**
     * Clears the country preference
     */
    suspend fun clearCountry()
}
