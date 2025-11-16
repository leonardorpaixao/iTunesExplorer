package com.itunesexplorer.catalog.presentation.details

import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.ITunesItem
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsViewState(
    val item: ITunesItem? = null,
    val relatedItems: List<ITunesItem> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState

sealed class DetailsIntent : ViewIntent {
    data object LoadDetails : DetailsIntent()
    data object Retry : DetailsIntent()
    data object OpenInStore : DetailsIntent()
}

sealed class DetailsEffect : ViewEffect {
    data class ShowError(val message: String) : DetailsEffect()
    data class OpenUrl(val url: String) : DetailsEffect()
}

class DetailsScreenModel(
    private val iTunesApi: ITunesApi,
    private val itemId: String
) : MviViewModel<DetailsViewState, DetailsIntent, DetailsEffect>(
    initialState = DetailsViewState()
) {

    init {
        onAction(DetailsIntent.LoadDetails)
    }

    override fun onAction(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadDetails -> loadDetails()
            is DetailsIntent.Retry -> loadDetails()
            is DetailsIntent.OpenInStore -> openInStore()
        }
    }

    private fun loadDetails() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val response = iTunesApi.details(id = itemId, limit = 10)

                val mainItem = response.results.firstOrNull()
                val related = if (response.results.size > 1) {
                    response.results.drop(1)
                } else {
                    emptyList()
                }

                mutableState.update {
                    it.copy(
                        isLoading = false,
                        item = mainItem,
                        relatedItems = related
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
                sendEffect(DetailsEffect.ShowError(errorMessage))
            }
        }
    }

    private fun openInStore() {
        val url = state.value.item?.trackViewUrl
            ?: state.value.item?.collectionViewUrl

        if (url != null) {
            sendEffect(DetailsEffect.OpenUrl(url))
        }
    }
}
