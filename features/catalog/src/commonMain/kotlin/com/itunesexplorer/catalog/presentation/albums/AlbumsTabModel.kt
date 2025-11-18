package com.itunesexplorer.catalog.presentation.albums

import com.itunesexplorer.catalog.domain.model.Album
import com.itunesexplorer.catalog.domain.model.MusicGenre
import com.itunesexplorer.catalog.domain.usecase.GetAlbumsByGenreUseCase
import com.itunesexplorer.catalog.domain.usecase.GetTopAlbumsUseCase
import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.settings.country.CountryManager
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlbumsViewState(
    val recommendations: List<Album> = emptyList(),
    val selectedGenre: MusicGenre = MusicGenre.ALL,
    val isLoading: Boolean = false,
    val error: DomainError? = null
) : ViewState

sealed class AlbumsIntent : ViewIntent {
    data object LoadRecommendations : AlbumsIntent()
    data class SelectGenre(val genre: MusicGenre) : AlbumsIntent()
    data object Retry : AlbumsIntent()
}

sealed class AlbumsEffect : ViewEffect {
    data class ShowError(val error: DomainError) : AlbumsEffect()
}

class AlbumsTabModel(
    private val getTopAlbumsUseCase: GetTopAlbumsUseCase,
    private val getAlbumsByGenreUseCase: GetAlbumsByGenreUseCase,
    private val countryManager: CountryManager
) : MviViewModel<AlbumsViewState, AlbumsIntent, AlbumsEffect>(
    initialState = AlbumsViewState()
) {

    init {
        onAction(AlbumsIntent.LoadRecommendations)

        screenModelScope.launch {
            countryManager.currentCountry
                .drop(1)
                .collect { country ->
                    if (country != null) {
                        onAction(AlbumsIntent.LoadRecommendations)
                    }
                }
        }
    }

    override fun onAction(intent: AlbumsIntent) {
        when (intent) {
            is AlbumsIntent.LoadRecommendations -> loadRecommendations()
            is AlbumsIntent.SelectGenre -> selectGenre(intent.genre)
            is AlbumsIntent.Retry -> {
                if (state.value.selectedGenre == MusicGenre.ALL) {
                    loadRecommendations()
                } else {
                    loadAlbumsByGenre(state.value.selectedGenre)
                }
            }
        }
    }

    private fun selectGenre(genre: MusicGenre) {
        mutableState.update { it.copy(selectedGenre = genre) }

        if (genre == MusicGenre.ALL) {
            loadRecommendations()
        } else {
            loadAlbumsByGenre(genre)
        }
    }

    private fun loadRecommendations() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            getTopAlbumsUseCase().fold(
                onSuccess = { albums ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            recommendations = albums
                        )
                    }
                },
                onFailure = { error ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                    sendEffect(AlbumsEffect.ShowError(error))
                }
            )
        }
    }

    private fun loadAlbumsByGenre(genre: MusicGenre) {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            getAlbumsByGenreUseCase(genre).fold(
                onSuccess = { albums ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            recommendations = albums
                        )
                    }
                },
                onFailure = { error ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                    sendEffect(AlbumsEffect.ShowError(error))
                }
            )
        }
    }
}
