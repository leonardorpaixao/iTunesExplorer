package com.itunesexplorer.listing.presentation

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.ITunesItem
import com.itunesexplorer.network.models.MediaType
import com.itunesexplorer.network.models.RssFeedEntry
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListingState(
    val isLoading: Boolean = false,
    val items: List<ITunesItem> = emptyList(),
    val recommendations: List<RssFeedEntry> = emptyList(),
    val isLoadingRecommendations: Boolean = false,
    val showRecommendations: Boolean = false,
    val error: String? = null,
    val searchQuery: String = "",
    val selectedMediaType: MediaType = MediaType.ALL
)

class ListingScreenModel(
    private val iTunesApi: ITunesApi
) : StateScreenModel<ListingState>(ListingState()) {

    init {
        loadRecommendations()
    }

    private fun loadRecommendations() {
        screenModelScope.launch {
            mutableState.update {
                it.copy(
                    isLoadingRecommendations = true,
                    showRecommendations = true
                )
            }

            try {
                val response = iTunesApi.topAlbums(limit = 30)
                mutableState.update {
                    it.copy(
                        isLoadingRecommendations = false,
                        recommendations = response.feed.entry
                    )
                }
            } catch (e: Exception) {
                mutableState.update {
                    it.copy(isLoadingRecommendations = false)
                }
            }
        }
    }

    fun search(query: String = state.value.searchQuery) {
        if (query.isBlank()) return

        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null, showRecommendations = false) }
            
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
                        items = response.results,
                        searchQuery = query
                    )
                }
            } catch (e: Exception) {
                mutableState.update { 
                    it.copy(
                        isLoading = false,
                        error = e.message ?: "An error occurred"
                    )
                }
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        mutableState.update { it.copy(searchQuery = query) }
    }
    
    fun selectMediaType(mediaType: MediaType) {
        mutableState.update { it.copy(selectedMediaType = mediaType) }
        if (state.value.searchQuery.isNotBlank()) {
            search()
        }
    }
    
    fun retry() {
        search()
    }

    fun surpriseMe() {
        mutableState.update {
            it.copy(
                showRecommendations = true,
                searchQuery = "",
                items = emptyList()
            )
        }
        if (state.value.recommendations.isEmpty() && !state.value.isLoadingRecommendations) {
            loadRecommendations()
        }
    }

    fun clearRecommendations() {
        mutableState.update { it.copy(showRecommendations = false) }
    }
}
