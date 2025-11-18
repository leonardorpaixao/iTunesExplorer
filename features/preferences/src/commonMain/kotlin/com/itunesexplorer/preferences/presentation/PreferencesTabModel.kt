package com.itunesexplorer.preferences.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.NoEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.i18n.Locales
import com.itunesexplorer.preferences.domain.Language
import com.itunesexplorer.settings.data.PreferencesRepository
import com.itunesexplorer.settings.language.LanguageManager
import com.itunesexplorer.settings.country.CountryManager
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PreferencesViewState(
    val availableLanguages: List<Language> = emptyList(),
    val selectedLanguage: String = Locales.EN,
    val pendingLanguage: String? = null,
    val showConfirmDialog: Boolean = false,
    val isLoading: Boolean = false,
    val availableCountries: List<com.itunesexplorer.preferences.domain.Country> = emptyList(),
    val selectedCountry: String = "US",
    val error: DomainError? = null
) : ViewState

sealed class PreferencesIntent : ViewIntent {
    data object LoadLanguages : PreferencesIntent()
    data class SelectLanguage(val languageCode: String) : PreferencesIntent()
    data object ConfirmLanguageChange : PreferencesIntent()
    data object DismissDialog : PreferencesIntent()
    data object LoadCountries : PreferencesIntent()
}

class PreferencesTabModel(
    private val preferencesRepository: PreferencesRepository
) : MviViewModel<PreferencesViewState, PreferencesIntent, NoEffect>(
    initialState = PreferencesViewState()
) {

    init {
        onAction(PreferencesIntent.LoadLanguages)
        onAction(PreferencesIntent.LoadCountries)

        screenModelScope.launch {
            CountryManager.currentCountry.collect { country ->
                mutableState.update { state ->
                    state.copy(selectedCountry = country ?: "")
                }
            }
        }
    }

    override fun onAction(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.LoadLanguages -> loadLanguages()
            is PreferencesIntent.SelectLanguage -> selectLanguage(intent.languageCode)
            is PreferencesIntent.ConfirmLanguageChange -> confirmLanguageChange()
            is PreferencesIntent.DismissDialog -> dismissDialog()
            is PreferencesIntent.LoadCountries -> loadCountries()
        }
    }

    private fun loadLanguages() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val savedLanguage = preferencesRepository.getLanguage()
                val currentLanguage = savedLanguage ?: LanguageManager.getCurrentLanguageTag() ?: Locales.EN

                val languages = listOf(
                    Language(Locales.EN, LANGUAGE_NAME_ENGLISH),
                    Language(Locales.PT_BR, LANGUAGE_NAME_PORTUGUESE_BRAZIL),
                    Language(Locales.PT_PT, LANGUAGE_NAME_PORTUGUESE_PORTUGAL),
                    Language(Locales.FR, LANGUAGE_NAME_FRENCH),
                    Language(Locales.ES, LANGUAGE_NAME_SPANISH),
                    Language(Locales.DE, LANGUAGE_NAME_GERMAN)
                )

                mutableState.update {
                    it.copy(
                        isLoading = false,
                        availableLanguages = languages,
                        selectedLanguage = currentLanguage
                    )
                }
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        error = DomainError.UnknownError(e.message ?: ERROR_FAILED_TO_LOAD_LANGUAGES)
                    )
                }
            }
        }
    }

    private fun selectLanguage(languageCode: String) {
        if (languageCode == state.value.selectedLanguage) {
            return
        }

        mutableState.update {
            it.copy(
                pendingLanguage = languageCode,
                showConfirmDialog = true
            )
        }
    }

    private fun confirmLanguageChange() {
        val pendingLanguage = state.value.pendingLanguage ?: return

        screenModelScope.launch {
            try {
                preferencesRepository.setLanguage(pendingLanguage)
                LanguageManager.setLanguage(pendingLanguage)

                mutableState.update {
                    it.copy(
                        selectedLanguage = pendingLanguage,
                        pendingLanguage = null,
                        showConfirmDialog = false,
                        error = null
                    )
                }
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        pendingLanguage = null,
                        showConfirmDialog = false,
                        error = DomainError.UnknownError(e.message ?: ERROR_FAILED_TO_CHANGE_LANGUAGE)
                    )
                }
            }
        }
    }

    private fun dismissDialog() {
        mutableState.update {
            it.copy(
                pendingLanguage = null,
                showConfirmDialog = false
            )
        }
    }

    private fun loadCountries() {
        screenModelScope.launch {
            try {
                val countries = com.itunesexplorer.preferences.domain.SupportedCountries.all
                val savedCountry = preferencesRepository.getCountry()

                mutableState.update {
                    it.copy(
                        availableCountries = countries,
                        selectedCountry = savedCountry ?: "", // Empty string for "None"
                        error = null
                    )
                }

                savedCountry?.let { CountryManager.initialize(it) }
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        error = DomainError.UnknownError(e.message ?: ERROR_FAILED_TO_LOAD_COUNTRIES)
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
