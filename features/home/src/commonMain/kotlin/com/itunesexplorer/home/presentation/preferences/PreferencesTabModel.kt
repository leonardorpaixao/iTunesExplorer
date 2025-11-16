package com.itunesexplorer.home.presentation.preferences

import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState

data class PreferencesViewState(
    val placeholder: String = ""
) : ViewState

sealed class PreferencesIntent : ViewIntent

sealed class PreferencesEffect : ViewEffect

class PreferencesTabModel : MviViewModel<PreferencesViewState, PreferencesIntent, PreferencesEffect>(
    initialState = PreferencesViewState()
) {
    override fun onAction(intent: PreferencesIntent) {
        // TODO: Implement preferences functionality
    }
}
