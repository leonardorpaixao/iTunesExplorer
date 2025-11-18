package com.itunesexplorer.catalog.presentation.search

import com.itunesexplorer.catalog.data.CatalogConstants
import com.itunesexplorer.catalog.domain.model.MediaType
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.SearchRepository
import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.NoEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.settings.country.CountryManager
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.flow.update
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
    data object Retry : SearchIntent()
}

class SearchTabModel(
    private val searchRepository: SearchRepository,
    private val countryManager: CountryManager
) : MviViewModel<SearchViewState, SearchIntent, NoEffect>(
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

            searchRepository.search(
                query = query,
                mediaType = state.value.selectedMediaType,
                limit = CatalogConstants.REQUEST_ITEMS_LIMIT
            ).fold(
                onSuccess = { items ->
                    val hasCountrySelected = countryManager.getCurrentCountryCode()?.isNotEmpty() == true
                    val showHint = items.isEmpty() && hasCountrySelected

                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            items = items,
                            showRegionHint = showHint
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
                }
            )
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
