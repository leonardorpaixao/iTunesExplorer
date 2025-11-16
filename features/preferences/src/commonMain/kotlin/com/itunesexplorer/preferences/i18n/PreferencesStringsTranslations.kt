package com.itunesexplorer.preferences.i18n

import cafe.adriel.lyricist.LyricistStrings
import com.itunesexplorer.i18n.Locales

@LyricistStrings(languageTag = Locales.EN, default = true)
val EnPreferencesStrings = PreferencesStrings(
    preferences = "Preferences",
    language = "Language",
    chooseLanguage = "Choose Language",
    confirmLanguageChange = "Change Language?",
    confirmLanguageMessage = "The app will reload to apply the new language.",
    confirm = "Confirm",
    cancel = "Cancel",
    languageName = { code ->
        when (code) {
            Locales.EN -> "English"
            Locales.PT_BR -> "Portuguese (Brazil)"
            Locales.PT_PT -> "Portuguese (Portugal)"
            Locales.FR -> "French"
            Locales.ES -> "Spanish"
            Locales.DE -> "German"
            else -> code
        }
    }
)

@LyricistStrings(languageTag = Locales.PT_BR, default = false)
val PtBrPreferencesStrings = PreferencesStrings(
    preferences = "Preferências",
    language = "Idioma",
    chooseLanguage = "Escolher Idioma",
    confirmLanguageChange = "Alterar Idioma?",
    confirmLanguageMessage = "O aplicativo será recarregado para aplicar o novo idioma.",
    confirm = "Confirmar",
    cancel = "Cancelar",
    languageName = { code ->
        when (code) {
            Locales.EN -> "Inglês"
            Locales.PT_BR -> "Português (Brasil)"
            Locales.PT_PT -> "Português (Portugal)"
            Locales.FR -> "Francês"
            Locales.ES -> "Espanhol"
            Locales.DE -> "Alemão"
            else -> code
        }
    }
)

@LyricistStrings(languageTag = Locales.PT_PT, default = false)
val PtPtPreferencesStrings = PreferencesStrings(
    preferences = "Preferências",
    language = "Idioma",
    chooseLanguage = "Escolher Idioma",
    confirmLanguageChange = "Alterar Idioma?",
    confirmLanguageMessage = "A aplicação será recarregada para aplicar o novo idioma.",
    confirm = "Confirmar",
    cancel = "Cancelar",
    languageName = { code ->
        when (code) {
            Locales.EN -> "Inglês"
            Locales.PT_BR -> "Português (Brasil)"
            Locales.PT_PT -> "Português (Portugal)"
            Locales.FR -> "Francês"
            Locales.ES -> "Espanhol"
            Locales.DE -> "Alemão"
            else -> code
        }
    }
)

@LyricistStrings(languageTag = Locales.FR, default = false)
val FrPreferencesStrings = PreferencesStrings(
    preferences = "Préférences",
    language = "Langue",
    chooseLanguage = "Choisir la Langue",
    confirmLanguageChange = "Changer de Langue?",
    confirmLanguageMessage = "L'application sera rechargée pour appliquer la nouvelle langue.",
    confirm = "Confirmer",
    cancel = "Annuler",
    languageName = { code ->
        when (code) {
            Locales.EN -> "Anglais"
            Locales.PT_BR -> "Portugais (Brésil)"
            Locales.PT_PT -> "Portugais (Portugal)"
            Locales.FR -> "Français"
            Locales.ES -> "Espagnol"
            Locales.DE -> "Allemand"
            else -> code
        }
    }
)

@LyricistStrings(languageTag = Locales.ES, default = false)
val EsPreferencesStrings = PreferencesStrings(
    preferences = "Preferencias",
    language = "Idioma",
    chooseLanguage = "Elegir Idioma",
    confirmLanguageChange = "¿Cambiar Idioma?",
    confirmLanguageMessage = "La aplicación se recargará para aplicar el nuevo idioma.",
    confirm = "Confirmar",
    cancel = "Cancelar",
    languageName = { code ->
        when (code) {
            Locales.EN -> "Inglés"
            Locales.PT_BR -> "Portugués (Brasil)"
            Locales.PT_PT -> "Portugués (Portugal)"
            Locales.FR -> "Francés"
            Locales.ES -> "Español"
            Locales.DE -> "Alemán"
            else -> code
        }
    }
)

@LyricistStrings(languageTag = Locales.DE, default = false)
val DePreferencesStrings = PreferencesStrings(
    preferences = "Einstellungen",
    language = "Sprache",
    chooseLanguage = "Sprache Wählen",
    confirmLanguageChange = "Sprache Ändern?",
    confirmLanguageMessage = "Die Anwendung wird neu geladen, um die neue Sprache anzuwenden.",
    confirm = "Bestätigen",
    cancel = "Abbrechen",
    languageName = { code ->
        when (code) {
            Locales.EN -> "Englisch"
            Locales.PT_BR -> "Portugiesisch (Brasilien)"
            Locales.PT_PT -> "Portugiesisch (Portugal)"
            Locales.FR -> "Französisch"
            Locales.ES -> "Spanisch"
            Locales.DE -> "Deutsch"
            else -> code
        }
    }
)
