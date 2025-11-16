package com.itunesexplorer.i18n

import kotlinx.browser.document

actual fun getSystemLanguage(): String {
    // WASM doesn't have direct access to browser locale
    // Default to English for now
    // In a real app, you'd use JS interop to get navigator.language
    return Locales.EN
}

actual fun setHtmlLangAttribute(languageTag: String) {
    // Update the HTML lang attribute for accessibility and SEO
    document.documentElement?.setAttribute("lang", languageTag)
}
