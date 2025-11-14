package com.itunesexplorer.i18n

actual fun getSystemLanguage(): String {
    // WASM doesn't have direct access to browser locale
    // Default to English for now
    // In a real app, you'd use JS interop to get navigator.language
    return Locales.EN
}
