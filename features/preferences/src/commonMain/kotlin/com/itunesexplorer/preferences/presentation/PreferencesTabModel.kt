package com.itunesexplorer.preferences.presentation

import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
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
    val selectedCountry: String = "US"
) : ViewState

sealed class PreferencesIntent : ViewIntent {
    data object LoadLanguages : PreferencesIntent()
    data class SelectLanguage(val languageCode: String) : PreferencesIntent()
    data object ConfirmLanguageChange : PreferencesIntent()
    data object DismissDialog : PreferencesIntent()
    data object LoadCountries : PreferencesIntent()
}

sealed class PreferencesEffect : ViewEffect {
    data class ShowError(val message: String) : PreferencesEffect()
}

class PreferencesTabModel(
    private val preferencesRepository: PreferencesRepository
) : MviViewModel<PreferencesViewState, PreferencesIntent, PreferencesEffect>(
    initialState = PreferencesViewState()
) {

    init {
        onAction(PreferencesIntent.LoadLanguages)
        onAction(PreferencesIntent.LoadCountries)

        // Observe country changes from CountryManager
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
            mutableState.update { it.copy(isLoading = true) }

            try {
                // Get saved language or use current from LanguageManager
                val savedLanguage = preferencesRepository.getLanguage()
                val currentLanguage = savedLanguage ?: LanguageManager.getCurrentLanguageTag() ?: Locales.EN

                // Define available languages with their native names
                val languages = listOf(
                    Language(Locales.EN, "English"),
                    Language(Locales.PT_BR, "Português (Brasil)"),
                    Language(Locales.PT_PT, "Português (Portugal)"),
                    Language(Locales.FR, "Français"),
                    Language(Locales.ES, "Español"),
                    Language(Locales.DE, "Deutsch")
                )

                mutableState.update {
                    it.copy(
                        isLoading = false,
                        availableLanguages = languages,
                        selectedLanguage = currentLanguage
                    )
                }
            } catch (e: Exception) {
                mutableState.update { it.copy(isLoading = false) }
                sendEffect(PreferencesEffect.ShowError(e.message ?: "Failed to load languages"))
            }
        }
    }

    private fun selectLanguage(languageCode: String) {
        // If selecting the same language, do nothing
        if (languageCode == state.value.selectedLanguage) {
            return
        }

        // Show confirmation dialog
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
                        showConfirmDialog = false
                    )
                }
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(
                        pendingLanguage = null,
                        showConfirmDialog = false
                    )
                }
                sendEffect(PreferencesEffect.ShowError(e.message ?: "Failed to change language"))
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
                // Get all available countries
                val countries = com.itunesexplorer.preferences.domain.SupportedCountries.all

                // Get saved country preference
                val savedCountry = preferencesRepository.getCountry()

                mutableState.update {
                    it.copy(
                        availableCountries = countries,
                        selectedCountry = savedCountry ?: "" // Empty string for "None"
                    )
                }

                // Initialize CountryManager if a country was saved
                savedCountry?.let { CountryManager.initialize(it) }
            } catch (e: Exception) {
                sendEffect(PreferencesEffect.ShowError(e.message ?: "Failed to load countries"))
            }
        }
    }
}
