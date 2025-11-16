package com.itunesexplorer.home.presentation.albums

import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.RssFeedEntry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlbumsViewState(
    val recommendations: List<RssFeedEntry> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState

sealed class AlbumsIntent : ViewIntent {
    data object LoadRecommendations : AlbumsIntent()
    data object Retry : AlbumsIntent()
}

sealed class AlbumsEffect : ViewEffect {
    data class ShowError(val message: String) : AlbumsEffect()
}

class AlbumsTabModel(
    private val iTunesApi: ITunesApi
) : MviViewModel<AlbumsViewState, AlbumsIntent, AlbumsEffect>(
    initialState = AlbumsViewState()
) {

    init {
        onAction(AlbumsIntent.LoadRecommendations)
    }

    override fun onAction(intent: AlbumsIntent) {
        when (intent) {
            is AlbumsIntent.LoadRecommendations -> loadRecommendations()
            is AlbumsIntent.Retry -> loadRecommendations()
        }
    }

    private fun loadRecommendations() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = iTunesApi.topAlbums(limit = 30)
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        recommendations = response.feed.entry
                    )
                }
            } catch (e: Exception) {
                val errorMessage = e.message ?: "An error occurred"
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        error = errorMessage
                    )
                }
                sendEffect(AlbumsEffect.ShowError(errorMessage))
            }
        }
    }
}
