package com.itunesexplorer.i18n

expect fun getSystemLanguage(): String

/**
 * Updates the HTML lang attribute (primarily for WASM/Web platform).
 * This is important for accessibility and SEO.
 * On non-web platforms (Android, iOS, Desktop), this is a no-op.
 */
expect fun setHtmlLangAttribute(languageTag: String)
