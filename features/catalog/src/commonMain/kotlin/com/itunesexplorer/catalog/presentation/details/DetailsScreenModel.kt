package com.itunesexplorer.catalog.presentation.details

import com.itunesexplorer.foundation.mvi.MviViewModel
import com.itunesexplorer.foundation.mvi.NoEffect
import com.itunesexplorer.foundation.mvi.ViewIntent
import com.itunesexplorer.foundation.mvi.ViewState
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.core.error.DomainError
import com.itunesexplorer.foundation.mvi.ViewEffect
import kotlinx.coroutines.launch

internal data class DetailsViewState(
    val item: SearchResult? = null,
    val isLoading: Boolean = false,
    val error: DomainError? = null
) : ViewState

internal sealed class DetailsIntent : ViewIntent {
    data object LoadDetails : DetailsIntent()
    data object Retry : DetailsIntent()
    data object Back: DetailsIntent()
}

internal sealed class DetailsEffect: ViewEffect {
    data object Back: DetailsEffect()
}

internal class DetailsScreenModel(
    private val detailsRepository: DetailsRepository,
    private val itemId: String
) : MviViewModel<DetailsViewState, DetailsIntent, DetailsEffect>(
    initialState = DetailsViewState(isLoading = true)
) {

    init {
        onAction(DetailsIntent.LoadDetails)
    }

    override fun onAction(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadDetails -> loadDetails()
            is DetailsIntent.Retry -> loadDetails()
            is DetailsIntent.Back -> sendEffect(DetailsEffect.Back)
        }
    }

    private fun loadDetails() {
        screenModelScope.launch {
            updateState { it.copy(isLoading = true, error = null) }

            detailsRepository.getItemDetails(itemId).fold(
                onSuccess = { itemDetails ->
                    updateState {
                        it.copy(
                            isLoading = false,
                            item = itemDetails,
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
}
