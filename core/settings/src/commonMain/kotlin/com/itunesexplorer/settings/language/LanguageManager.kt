package com.itunesexplorer.settings.language

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton manager for handling language changes across the app
 */
object LanguageManager {
    private val _currentLanguage = MutableStateFlow<String?>(null)

    /**
     * StateFlow emitting the current language tag.
     * Null means not yet initialized.
     */
    val currentLanguage: StateFlow<String?> = _currentLanguage.asStateFlow()

    /**
     * Updates the current language and triggers app-wide recomposition
     * @param languageTag The new language tag (e.g., "en", "pt-BR")
     */
    fun setLanguage(languageTag: String) {
        println("🌐 [LanguageManager] setLanguage called with: $languageTag")
        println("🌐 [LanguageManager] Current value before: ${_currentLanguage.value}")
        _currentLanguage.value = languageTag
        println("🌐 [LanguageManager] Current value after: ${_currentLanguage.value}")
    }

    /**
     * Initializes the language manager with the given language tag
     * @param languageTag The initial language tag
     */
    fun initialize(languageTag: String) {
        println("🎬 [LanguageManager] initialize called with: $languageTag")
        println("🎬 [LanguageManager] Current value: ${_currentLanguage.value}")
        if (_currentLanguage.value == null) {
            _currentLanguage.value = languageTag
            println("✅ [LanguageManager] Initialized to: $languageTag")
        } else {
            println("⏭️  [LanguageManager] Already initialized, skipping")
        }
    }

    /**
     * Gets the current language tag or null if not set
     */
    fun getCurrentLanguageTag(): String? = _currentLanguage.value
}
