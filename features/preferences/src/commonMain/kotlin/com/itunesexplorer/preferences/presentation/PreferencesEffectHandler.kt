package com.itunesexplorer.preferences.presentation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import cafe.adriel.voyager.navigator.bottomSheet.LocalBottomSheetNavigator
import com.itunesexplorer.preferences.presentation.i18n.PreferencesStrings
import kotlinx.coroutines.flow.Flow

@Composable
internal fun PreferencesEffectHandler(
    effectFlow: Flow<PreferencesEffect>,
    onAction: (PreferencesIntent) -> Unit,
    strings: PreferencesStrings
) {
    var showLanguageDialog by remember { mutableStateOf<String?>(null) }
    val bottomSheetNavigator = LocalBottomSheetNavigator.current

    LaunchedEffect(Unit) {
        effectFlow.collect { effect ->
            when (effect) {
                is PreferencesEffect.ShowLanguageConfirmDialog -> {
                    showLanguageDialog = effect.languageCode
                }

                is PreferencesEffect.OpenCountrySelectionModal -> {
                    bottomSheetNavigator.show(
                        CountrySelectionModal(selectedCountry = effect.selectedCountry)
                    )
                }
            }
        }
    }

    showLanguageDialog?.let { languageCode ->
        LanguageChangeDialog(
            onConfirm = {
                onAction(PreferencesIntent.ConfirmLanguageChange(languageCode))
                showLanguageDialog = null
            },
            onDismiss = { showLanguageDialog = null },
            strings = strings
        )
    }
}