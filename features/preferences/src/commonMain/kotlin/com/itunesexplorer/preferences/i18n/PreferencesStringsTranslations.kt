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
    },
    country = "Country",
    chooseCountry = "Choose Country",
    currentCountry = "Current Country",
    countryHelpText = "The selected country affects search results. Content availability varies by region.",
    error = "Error",
    countryName = { code ->
        when (code.uppercase()) {
            "" -> "None"
            "AE" -> "United Arab Emirates"
            "AR" -> "Argentina"
            "AT" -> "Austria"
            "AU" -> "Australia"
            "BE" -> "Belgium"
            "BR" -> "Brazil"
            "CA" -> "Canada"
            "CH" -> "Switzerland"
            "CL" -> "Chile"
            "CN" -> "China"
            "CO" -> "Colombia"
            "DE" -> "Germany"
            "DK" -> "Denmark"
            "ES" -> "Spain"
            "FI" -> "Finland"
            "FR" -> "France"
            "GB" -> "United Kingdom"
            "IN" -> "India"
            "IT" -> "Italy"
            "JP" -> "Japan"
            "KR" -> "South Korea"
            "MX" -> "Mexico"
            "NL" -> "Netherlands"
            "NO" -> "Norway"
            "NZ" -> "New Zealand"
            "PL" -> "Poland"
            "PT" -> "Portugal"
            "RU" -> "Russia"
            "SE" -> "Sweden"
            "SG" -> "Singapore"
            "TH" -> "Thailand"
            "US" -> "United States"
            "ZA" -> "South Africa"
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
    },
    country = "País",
    chooseCountry = "Escolher País",
    currentCountry = "País Atual",
    countryHelpText = "O país selecionado influencia diretamente os resultados da pesquisa. A disponibilidade de conteúdo varia por região.",
    error = "Erro",
    countryName = { code ->
        when (code.uppercase()) {
            "" -> "Nenhum"
            "AE" -> "Emirados Árabes Unidos"
            "AR" -> "Argentina"
            "AT" -> "Áustria"
            "AU" -> "Austrália"
            "BE" -> "Bélgica"
            "BR" -> "Brasil"
            "CA" -> "Canadá"
            "CH" -> "Suíça"
            "CL" -> "Chile"
            "CN" -> "China"
            "CO" -> "Colômbia"
            "DE" -> "Alemanha"
            "DK" -> "Dinamarca"
            "ES" -> "Espanha"
            "FI" -> "Finlândia"
            "FR" -> "França"
            "GB" -> "Reino Unido"
            "IN" -> "Índia"
            "IT" -> "Itália"
            "JP" -> "Japão"
            "KR" -> "Coreia do Sul"
            "MX" -> "México"
            "NL" -> "Holanda"
            "NO" -> "Noruega"
            "NZ" -> "Nova Zelândia"
            "PL" -> "Polônia"
            "PT" -> "Portugal"
            "RU" -> "Rússia"
            "SE" -> "Suécia"
            "SG" -> "Singapura"
            "TH" -> "Tailândia"
            "US" -> "Estados Unidos"
            "ZA" -> "África do Sul"
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
    },
    country = "País",
    chooseCountry = "Escolher País",
    currentCountry = "País Actual",
    countryHelpText = "O país selecionado influencia directamente os resultados da pesquisa. A disponibilidade de conteúdo varia por região.",
    error = "Erro",
    countryName = { code ->
        when (code.uppercase()) {
            "" -> "Nenhum"
            "AE" -> "Emirados Árabes Unidos"
            "AR" -> "Argentina"
            "AT" -> "Áustria"
            "AU" -> "Austrália"
            "BE" -> "Bélgica"
            "BR" -> "Brasil"
            "CA" -> "Canadá"
            "CH" -> "Suíça"
            "CL" -> "Chile"
            "CN" -> "China"
            "CO" -> "Colômbia"
            "DE" -> "Alemanha"
            "DK" -> "Dinamarca"
            "ES" -> "Espanha"
            "FI" -> "Finlândia"
            "FR" -> "França"
            "GB" -> "Reino Unido"
            "IN" -> "Índia"
            "IT" -> "Itália"
            "JP" -> "Japão"
            "KR" -> "Coreia do Sul"
            "MX" -> "México"
            "NL" -> "Holanda"
            "NO" -> "Noruega"
            "NZ" -> "Nova Zelândia"
            "PL" -> "Polónia"
            "PT" -> "Portugal"
            "RU" -> "Rússia"
            "SE" -> "Suécia"
            "SG" -> "Singapura"
            "TH" -> "Tailândia"
            "US" -> "Estados Unidos"
            "ZA" -> "África do Sul"
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
    },
    country = "Pays",
    chooseCountry = "Choisir le Pays",
    currentCountry = "Pays Actuel",
    countryHelpText = "Le pays sélectionné influence directement les résultats de recherche. La disponibilité du contenu varie selon les régions.",
    error = "Erreur",
    countryName = { code ->
        when (code.uppercase()) {
            "" -> "Aucun"
            "AE" -> "Émirats Arabes Unis"
            "AR" -> "Argentine"
            "AT" -> "Autriche"
            "AU" -> "Australie"
            "BE" -> "Belgique"
            "BR" -> "Brésil"
            "CA" -> "Canada"
            "CH" -> "Suisse"
            "CL" -> "Chili"
            "CN" -> "Chine"
            "CO" -> "Colombie"
            "DE" -> "Allemagne"
            "DK" -> "Danemark"
            "ES" -> "Espagne"
            "FI" -> "Finlande"
            "FR" -> "France"
            "GB" -> "Royaume-Uni"
            "IN" -> "Inde"
            "IT" -> "Italie"
            "JP" -> "Japon"
            "KR" -> "Corée du Sud"
            "MX" -> "Mexique"
            "NL" -> "Pays-Bas"
            "NO" -> "Norvège"
            "NZ" -> "Nouvelle-Zélande"
            "PL" -> "Pologne"
            "PT" -> "Portugal"
            "RU" -> "Russie"
            "SE" -> "Suède"
            "SG" -> "Singapour"
            "TH" -> "Thaïlande"
            "US" -> "États-Unis"
            "ZA" -> "Afrique du Sud"
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
    },
    country = "País",
    chooseCountry = "Elegir País",
    currentCountry = "País Actual",
    countryHelpText = "El país seleccionado influye directamente en los resultados de búsqueda. La disponibilidad de contenido varía según la región.",
    error = "Error",
    countryName = { code ->
        when (code.uppercase()) {
            "" -> "Ninguno"
            "AE" -> "Emiratos Árabes Unidos"
            "AR" -> "Argentina"
            "AT" -> "Austria"
            "AU" -> "Australia"
            "BE" -> "Bélgica"
            "BR" -> "Brasil"
            "CA" -> "Canadá"
            "CH" -> "Suiza"
            "CL" -> "Chile"
            "CN" -> "China"
            "CO" -> "Colombia"
            "DE" -> "Alemania"
            "DK" -> "Dinamarca"
            "ES" -> "España"
            "FI" -> "Finlandia"
            "FR" -> "Francia"
            "GB" -> "Reino Unido"
            "IN" -> "India"
            "IT" -> "Italia"
            "JP" -> "Japón"
            "KR" -> "Corea del Sur"
            "MX" -> "México"
            "NL" -> "Países Bajos"
            "NO" -> "Noruega"
            "NZ" -> "Nueva Zelanda"
            "PL" -> "Polonia"
            "PT" -> "Portugal"
            "RU" -> "Rusia"
            "SE" -> "Suecia"
            "SG" -> "Singapur"
            "TH" -> "Tailandia"
            "US" -> "Estados Unidos"
            "ZA" -> "Sudáfrica"
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
    },
    country = "Land",
    chooseCountry = "Land Wählen",
    currentCountry = "Aktuelles Land",
    countryHelpText = "Das ausgewählte Land beeinflusst direkt die Suchergebnisse. Die Verfügbarkeit von Inhalten variiert je nach Region.",
    error = "Fehler",
    countryName = { code ->
        when (code.uppercase()) {
            "" -> "Keine"
            "AE" -> "Vereinigte Arabische Emirate"
            "AR" -> "Argentinien"
            "AT" -> "Österreich"
            "AU" -> "Australien"
            "BE" -> "Belgien"
            "BR" -> "Brasilien"
            "CA" -> "Kanada"
            "CH" -> "Schweiz"
            "CL" -> "Chile"
            "CN" -> "China"
            "CO" -> "Kolumbien"
            "DE" -> "Deutschland"
            "DK" -> "Dänemark"
            "ES" -> "Spanien"
            "FI" -> "Finnland"
            "FR" -> "Frankreich"
            "GB" -> "Vereinigtes Königreich"
            "IN" -> "Indien"
            "IT" -> "Italien"
            "JP" -> "Japan"
            "KR" -> "Südkorea"
            "MX" -> "Mexiko"
            "NL" -> "Niederlande"
            "NO" -> "Norwegen"
            "NZ" -> "Neuseeland"
            "PL" -> "Polen"
            "PT" -> "Portugal"
            "RU" -> "Russland"
            "SE" -> "Schweden"
            "SG" -> "Singapur"
            "TH" -> "Thailand"
            "US" -> "Vereinigte Staaten"
            "ZA" -> "Südafrika"
            else -> code
        }
    }
)
