package com.itunesexplorer.catalog.presentation.details

import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.NoEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.catalog.domain.model.SearchResult
import com.itunesexplorer.catalog.domain.repository.DetailsRepository
import com.itunesexplorer.core.error.DomainError
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class DetailsViewState(
    val item: SearchResult? = null,
    val isLoading: Boolean = false,
    val error: DomainError? = null
) : ViewState

sealed class DetailsIntent : ViewIntent {
    data object LoadDetails : DetailsIntent()
    data object Retry : DetailsIntent()
}

class DetailsScreenModel(
    private val detailsRepository: DetailsRepository,
    private val itemId: String
) : MviViewModel<DetailsViewState, DetailsIntent, NoEffect>(
    initialState = DetailsViewState(isLoading = true)
) {

    init {
        onAction(DetailsIntent.LoadDetails)
    }

    override fun onAction(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadDetails -> loadDetails()
            is DetailsIntent.Retry -> loadDetails()
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
                            item = itemDetails,
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
}
