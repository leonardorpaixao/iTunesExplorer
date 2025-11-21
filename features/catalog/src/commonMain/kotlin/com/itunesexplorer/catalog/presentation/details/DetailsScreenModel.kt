package com.itunesexplorer.catalog.presentation.details

import com.itunesexplorer.foundation.mvi.MviViewModel
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
    data class LoadDetails(val itemId: String) : DetailsIntent()
    data object Retry : DetailsIntent()
    data object Back: DetailsIntent()
}

internal sealed class DetailsEffect: ViewEffect {
    data object Back: DetailsEffect()
}

internal class DetailsScreenModel(
    private val detailsRepository: DetailsRepository,

) : MviViewModel<DetailsViewState, DetailsIntent, DetailsEffect>(
    initialState = DetailsViewState(isLoading = true)
) {

    private lateinit var itemId: String


    override fun onAction(intent: DetailsIntent) {
        when (intent) {
            is DetailsIntent.LoadDetails -> loadDetails(intent.itemId)
            is DetailsIntent.Retry -> loadDetails(itemId)
            is DetailsIntent.Back -> sendEffect(DetailsEffect.Back)
        }
    }

    private fun loadDetails(itemId: String) {
        this.itemId = itemId
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
