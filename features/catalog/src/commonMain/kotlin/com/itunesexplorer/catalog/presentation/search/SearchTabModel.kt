package com.itunesexplorer.catalog.presentation.search

import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.ITunesItem
import com.itunesexplorer.network.models.MediaType
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class SearchViewState(
    val isLoading: Boolean = false,
    val items: List<ITunesItem> = emptyList(),
    val error: String? = null,
    val selectedMediaType: MediaType = MediaType.ALL,
    val searchQuery: String = ""
) : ViewState

sealed class SearchIntent : ViewIntent {
    data class UpdateSearchQuery(val query: String) : SearchIntent()
    data object Search : SearchIntent()
    data class SelectMediaType(val mediaType: MediaType) : SearchIntent()
    data object Retry : SearchIntent()
}

sealed class SearchEffect : ViewEffect {
    data class ShowError(val message: String) : SearchEffect()
}

class SearchTabModel(
    private val iTunesApi: ITunesApi
) : MviViewModel<SearchViewState, SearchIntent, SearchEffect>(
    initialState = SearchViewState()
) {

    override fun onAction(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is SearchIntent.Search -> search()
            is SearchIntent.SelectMediaType -> selectMediaType(intent.mediaType)
            is SearchIntent.Retry -> retry()
        }
    }

    private fun updateSearchQuery(query: String) {
        mutableState.update { it.copy(searchQuery = query) }
    }

    private fun search() {
        val query = state.value.searchQuery
        if (query.isBlank()) {
            return
        }

        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val mediaType = if (state.value.selectedMediaType == MediaType.ALL) {
                    null
                } else {
                    state.value.selectedMediaType.value
                }

                val response = iTunesApi.search(
                    term = query,
                    media = mediaType,
                    limit = 50
                )

                mutableState.update {
                    it.copy(
                        isLoading = false,
                        items = response.results
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
                sendEffect(SearchEffect.ShowError(errorMessage))
            }
        }
    }

    private fun selectMediaType(mediaType: MediaType) {
        mutableState.update { it.copy(selectedMediaType = mediaType) }
        if (state.value.searchQuery.isNotBlank()) {
            search()
        }
    }

    private fun retry() {
        if (state.value.searchQuery.isNotBlank()) {
            search()
        }
    }
}
