package com.itunesexplorer.home.presentation

import com.itunesexplorer.foundation.mvi.MviViewModel
import com.itunesexplorer.foundation.mvi.NoEffect
import com.itunesexplorer.foundation.mvi.ViewIntent
import com.itunesexplorer.foundation.mvi.ViewState

enum class HomeTab {
    ALBUMS,
    SEARCH,
    PREFERENCES
}

data class HomeViewState(
    val selectedTab: HomeTab = HomeTab.ALBUMS
) : ViewState

sealed class HomeIntent : ViewIntent {
    data class SelectTab(val tab: HomeTab) : HomeIntent()
}

class HomeScreenModel : MviViewModel<HomeViewState, HomeIntent, NoEffect>(
    initialState = HomeViewState()
) {
    override fun onAction(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.SelectTab -> selectTab(intent.tab)
        }
    }

    private fun selectTab(tab: HomeTab) {
        updateState { it.copy(selectedTab = tab) }
    }
}
