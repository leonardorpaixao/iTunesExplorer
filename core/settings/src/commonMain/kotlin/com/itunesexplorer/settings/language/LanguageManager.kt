package com.itunesexplorer.settings.language

import com.itunesexplorer.i18n.setHtmlLangAttribute
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
        _currentLanguage.value = languageTag
        setHtmlLangAttribute(languageTag)
    }

    /**
     * Initializes the language manager with the given language tag
     * @param languageTag The initial language tag
     */
    fun initialize(languageTag: String) {
        if (_currentLanguage.value == null) {
            _currentLanguage.value = languageTag
            setHtmlLangAttribute(languageTag)
        }
    }

    /**
     * Gets the current language tag or null if not set
     */
    fun getCurrentLanguageTag(): String? = _currentLanguage.value

    /**
     * Converts the current language tag to iTunes API format (e.g., "pt-BR" -> "pt_br")
     * @return The language tag in iTunes API format, or "en_us" as default
     */
    fun getITunesLanguageCode(): String {
        val languageTag = _currentLanguage.value ?: return "en_us"

        // Convert language tag format (pt-BR, pt-PT) to iTunes format (pt_br, pt_pt)
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
