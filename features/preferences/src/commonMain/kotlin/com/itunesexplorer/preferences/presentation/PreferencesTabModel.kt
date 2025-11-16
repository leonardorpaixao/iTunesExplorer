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
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PreferencesViewState(
    val availableLanguages: List<Language> = emptyList(),
    val selectedLanguage: String = Locales.EN,
    val pendingLanguage: String? = null,
    val showConfirmDialog: Boolean = false,
    val isLoading: Boolean = false
) : ViewState

sealed class PreferencesIntent : ViewIntent {
    data object LoadLanguages : PreferencesIntent()
    data class SelectLanguage(val languageCode: String) : PreferencesIntent()
    data object ConfirmLanguageChange : PreferencesIntent()
    data object DismissDialog : PreferencesIntent()
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
    }

    override fun onAction(intent: PreferencesIntent) {
        when (intent) {
            is PreferencesIntent.LoadLanguages -> loadLanguages()
            is PreferencesIntent.SelectLanguage -> selectLanguage(intent.languageCode)
            is PreferencesIntent.ConfirmLanguageChange -> confirmLanguageChange()
            is PreferencesIntent.DismissDialog -> dismissDialog()
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
                println("🔄 [PreferencesTabModel] Confirming language change to: $pendingLanguage")

                // Save the new language preference
                preferencesRepository.setLanguage(pendingLanguage)
                println("💾 [PreferencesTabModel] Language saved to preferences: $pendingLanguage")

                // Update the language manager (this will trigger app reload)
                LanguageManager.setLanguage(pendingLanguage)
                println("✅ [PreferencesTabModel] LanguageManager updated to: $pendingLanguage")

                // Update local state
                mutableState.update {
                    it.copy(
                        selectedLanguage = pendingLanguage,
                        pendingLanguage = null,
                        showConfirmDialog = false
                    )
                }
            } catch (e: Exception) {
                println("❌ [PreferencesTabModel] Error changing language: ${e.message}")
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
}
