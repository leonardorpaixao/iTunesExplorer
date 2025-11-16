package com.itunesexplorer.preferences.domain

/**
 * Represents a language option in the preferences
 * @param code Language code (e.g., "en", "pt-BR")
 * @param nativeName Native name of the language (e.g., "English", "Português (Brasil)")
 */
data class Language(
    val code: String,
    val nativeName: String
)
