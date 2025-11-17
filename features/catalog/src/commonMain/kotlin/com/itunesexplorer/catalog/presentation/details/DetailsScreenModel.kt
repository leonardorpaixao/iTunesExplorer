package com.itunesexplorer.catalog.presentation.details

import com.itunesexplorer.catalog.presentation.toMessage
import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsViewState(
    val item: SearchResult? = null,
    val relatedItems: List<SearchResult> = emptyList(),
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
    private val detailsRepository: DetailsRepository,
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

            detailsRepository.getItemDetails(itemId).fold(
                onSuccess = { itemDetails ->
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            item = itemDetails.mainItem,
                            relatedItems = itemDetails.relatedItems
                        )
                    }
                },
                onFailure = { error ->
                    val errorMessage = error.toMessage()
                    mutableState.update {
                        it.copy(
                            isLoading = false,
                            error = errorMessage
                        )
                    }
                    sendEffect(DetailsEffect.ShowError(errorMessage))
                }
            )
        }
    }

    private fun openInStore() {
        val url = state.value.item?.viewUrl

        if (url != null) {
            sendEffect(DetailsEffect.OpenUrl(url))
        }
    }
}
