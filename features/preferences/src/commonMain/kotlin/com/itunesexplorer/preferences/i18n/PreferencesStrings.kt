package com.itunesexplorer.preferences.i18n

data class PreferencesStrings(
    val preferences: String,
    val language: String,
    val chooseLanguage: String,
    val confirmLanguageChange: String,
    val confirmLanguageMessage: String,
    val confirm: String,
    val cancel: String,
    val languageName: (String) -> String,
    val country: String,
    val chooseCountry: String,
    val currentCountry: String,
    val countryName: (String) -> String,
    val countryHelpText: String,
    val error: String
)
