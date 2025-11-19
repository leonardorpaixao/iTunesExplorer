package com.itunesexplorer.settings

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

/**
 * Singleton manager for handling language changes across the app
 */
object LanguageManager {
    private val _currentLanguage = MutableStateFlow<String?>(null)

    val currentLanguage: StateFlow<String?> = _currentLanguage.asStateFlow()

    fun setLanguage(languageTag: String) {
        _currentLanguage.value = languageTag
    }

    fun initialize(languageTag: String) {
        if (_currentLanguage.value == null) {
            _currentLanguage.value = languageTag
        }
    }

    fun getCurrentLanguageTag(): String? = _currentLanguage.value

    /**
     * Converts the current language tag to iTunes API format (e.g., "pt-BR" -> "pt_br")
     * @return The language tag in iTunes API format, or "en_us" as default
     */
    fun getITunesLanguageCode(): String {
        val languageTag = _currentLanguage.value ?: return "en_us"

        return when (languageTag) {
            "pt-BR" -> "pt_br"
            "pt-PT" -> "pt_pt"
            "en" -> "en_us"
            "fr" -> "fr_fr"
            "es" -> "es_es"
            "de" -> "de_de"
            else -> languageTag.lowercase().replace("-", "_")
        }
    }
}