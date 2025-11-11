package com.itunesexplorer.listing.presentation

import cafe.adriel.voyager.core.model.StateScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.error.Result
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.ITunesItem
import com.itunesexplorer.network.models.MediaType
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ListingState(
    val isLoading: Boolean = false,
    val items: List<ITunesItem> = emptyList(),
    val error: String? = null,
    val searchQuery: String = "",
    val selectedMediaType: MediaType = MediaType.ALL
)

class ListingScreenModel(
    private val iTunesApi: ITunesApi
) : StateScreenModel<ListingState>(ListingState()) {
    
    fun search(query: String = state.value.searchQuery) {
        if (query.isBlank()) return
        
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
}
