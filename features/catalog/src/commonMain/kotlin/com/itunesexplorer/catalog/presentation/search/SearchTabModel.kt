package com.itunesexplorer.catalog.presentation.search

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.foundation.mvi.MviViewModel
import com.itunesexplorer.foundation.mvi.ViewEffect
import com.itunesexplorer.foundation.mvi.ViewIntent
import com.itunesexplorer.foundation.mvi.ViewState
import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.settings.CountryManager
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.launch

data class SearchViewState(
    val isLoading: Boolean = false,
    val items: List<SearchResult> = emptyList(),
    val error: DomainError? = null,
    val selectedMediaType: MediaType = MediaType.ALL,
    val searchQuery: String = "",
    val showRegionHint: Boolean = false
) : ViewState

sealed class SearchIntent : ViewIntent {
    data class UpdateSearchQuery(val query: String) : SearchIntent()
    data object Search : SearchIntent()
    data class SelectMediaType(val mediaType: MediaType) : SearchIntent()
    data class ItemClicked(val itemId: String) : SearchIntent()
    data object Retry : SearchIntent()
}

sealed class SearchEffect : ViewEffect {
    data class NavigateToDetails(val itemId: String) : SearchEffect()
}

class SearchTabModel(
    private val searchRepository: SearchRepository,
    private val countryManager: CountryManager
) : MviViewModel<SearchViewState, SearchIntent, SearchEffect>(
    initialState = SearchViewState()
) {

    override fun onAction(intent: SearchIntent) {
        when (intent) {
            is SearchIntent.UpdateSearchQuery -> updateSearchQuery(intent.query)
            is SearchIntent.Search -> search()
            is SearchIntent.SelectMediaType -> selectMediaType(intent.mediaType)
            is SearchIntent.ItemClicked -> sendEffect(SearchEffect.NavigateToDetails(intent.itemId))
            is SearchIntent.Retry -> retry()
        }
    }

    private fun updateSearchQuery(query: String) {
        updateState { it.copy(searchQuery = query) }
    }

    private fun search() {
        val query = state.value.searchQuery
        if (query.isBlank()) {
            return
        }

        screenModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }

            searchRepository.search(
                query = query,
                mediaType = state.value.selectedMediaType,
                limit = CatalogConstants.REQUEST_ITEMS_LIMIT
            ).fold(
                onSuccess = { items ->
                    val hasCountrySelected = countryManager.getCurrentCountryCode()?.isNotEmpty() == true
                    val showHint = items.isEmpty() && hasCountrySelected

                    updateState {
                        it.copy(
                            isLoading = false,
                            items = items,
                            showRegionHint = showHint
                        )
                    }
                },
                onFailure = { error ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            error = error
                        )
                    }
                }
            )
        }
    }

    private fun selectMediaType(mediaType: MediaType) {
        updateState { it.copy(selectedMediaType = mediaType) }
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
