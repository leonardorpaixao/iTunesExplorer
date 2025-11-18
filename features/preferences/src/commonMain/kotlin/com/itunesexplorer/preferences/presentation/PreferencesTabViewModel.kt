package com.itunesexplorer.preferences.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.common.extensions.orEmpty
import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.i18n.Locales
import com.itunesexplorer.preferences.domain.Language
import com.itunesexplorer.preferences.domain.SupportedCountries
import com.itunesexplorer.settings.data.PreferencesRepository
import com.itunesexplorer.settings.language.LanguageManager
import com.itunesexplorer.settings.country.CountryManager
import kotlinx.coroutines.launch

data class PreferencesViewState(
    val availableLanguages: List<Language> = emptyList(),
    val selectedLanguage: String = Locales.EN,
    val isLoading: Boolean = false,
    val availableCountries: List<com.itunesexplorer.preferences.domain.Country> = emptyList(),
    val selectedCountry: String = PATTERN_COUNTRY,
    val error: DomainError? = null
) : ViewState {
    companion object {
        const val PATTERN_COUNTRY = "US"
    }
}

sealed class PreferencesIntent : ViewIntent {
    data object LoadLanguages : PreferencesIntent()
    data class SelectLanguage(val languageCode: String) : PreferencesIntent()
    data class ConfirmLanguageChange(val languageCode: String) : PreferencesIntent()
    data class OpenCountrySelectionModal(val selectedCountry: String) : PreferencesIntent()
    data object LoadCountries : PreferencesIntent()
}

sealed class PreferencesEffect : ViewEffect {
    data class ShowLanguageConfirmDialog(val languageCode: String) : PreferencesEffect()
    data class OpenCountrySelectionModal(val selectedCountry: String) : PreferencesEffect()
}

class PreferencesTabViewModel(
    private val preferencesRepository: PreferencesRepository
) : MviViewModel<PreferencesViewState, PreferencesIntent, PreferencesEffect>(
    initialState = PreferencesViewState()
) {

    init {
        onAction(PreferencesIntent.LoadLanguages)
        onAction(PreferencesIntent.LoadCountries)

        screenModelScope.launch {
            CountryManager.currentCountry.collect { country ->
                updateState {
                    it.copy(selectedCountry = country.orEmpty())
                }
            }
        }
    }

    override fun onAction(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.LoadLanguages -> loadLanguages()
            is PreferencesIntent.SelectLanguage -> selectLanguage(intent.languageCode)
            is PreferencesIntent.ConfirmLanguageChange -> confirmLanguageChange(intent.languageCode)
            is PreferencesIntent.LoadCountries -> loadCountries()
            is PreferencesIntent.OpenCountrySelectionModal -> sendEffect(
                PreferencesEffect.OpenCountrySelectionModal(
                    intent.selectedCountry
                )
            )
        }
    }

    private fun loadLanguages() {
        screenModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }

            try {
                val savedLanguage = preferencesRepository.getLanguage()
                val currentLanguage =
                    savedLanguage ?: LanguageManager.getCurrentLanguageTag() ?: Locales.EN

                val languages = listOf(
                    Language(Locales.EN, LANGUAGE_NAME_ENGLISH),
                    Language(Locales.PT_BR, LANGUAGE_NAME_PORTUGUESE_BRAZIL),
                    Language(Locales.PT_PT, LANGUAGE_NAME_PORTUGUESE_PORTUGAL),
                    Language(Locales.FR, LANGUAGE_NAME_FRENCH),
                    Language(Locales.ES, LANGUAGE_NAME_SPANISH),
                    Language(Locales.DE, LANGUAGE_NAME_GERMAN)
                )

                updateState { state ->
                    state.copy(
                        isLoading = false,
                        availableLanguages = languages,
                        selectedLanguage = currentLanguage
                    )
                }
            } catch (e: Exception) {
                updateState { state ->
                    state.copy(
                        isLoading = false,
                        error = DomainError.UnknownError(
                            e.message ?: ERROR_FAILED_TO_LOAD_LANGUAGES
                        )
                    )
                }
            }
        }
    }

    private fun selectLanguage(languageCode: String) {
        if (languageCode == state.value.selectedLanguage) {
            return
        }

        sendEffect(PreferencesEffect.ShowLanguageConfirmDialog(languageCode))
    }

    private fun confirmLanguageChange(languageCode: String) {
        screenModelScope.launch {
            try {
                preferencesRepository.setLanguage(languageCode)
                LanguageManager.setLanguage(languageCode)

                updateState {
                    it.copy(
                        selectedLanguage = languageCode,
                        error = null
                    )
                }
            } catch (e: Exception) {
                updateState {
                    it.copy(
                        error = DomainError.UnknownError(
                            e.message ?: ERROR_FAILED_TO_CHANGE_LANGUAGE
                        )
                    )
                }
            }
        }
    }

    private fun loadCountries() {
        screenModelScope.launch {
            runCatching {
                val countries = SupportedCountries.all
                val savedCountry = preferencesRepository.getCountry()

                updateState { state ->
                    state.copy(
                        availableCountries = countries,
                        selectedCountry = savedCountry.orEmpty(),
                        error = null
                    )
                }

                savedCountry?.let { CountryManager.initialize(it) }
            }.getOrElse { e ->
                updateState { state ->
                    state.copy(
                        error = DomainError.UnknownError(
                            e.message ?: ERROR_FAILED_TO_LOAD_COUNTRIES
                        )
                    )
                }
            }
        }
    }

    companion object {
        private const val LANGUAGE_NAME_ENGLISH = "English"
        private const val LANGUAGE_NAME_PORTUGUESE_BRAZIL = "Português (Brasil)"
        private const val LANGUAGE_NAME_PORTUGUESE_PORTUGAL = "Português (Portugal)"
        private const val LANGUAGE_NAME_FRENCH = "Français"
        private const val LANGUAGE_NAME_SPANISH = "Español"
        private const val LANGUAGE_NAME_GERMAN = "Deutsch"

        private const val ERROR_FAILED_TO_LOAD_LANGUAGES = "Failed to load languages"
        private const val ERROR_FAILED_TO_CHANGE_LANGUAGE = "Failed to change language"
        private const val ERROR_FAILED_TO_LOAD_COUNTRIES = "Failed to load countries"
    }
}
