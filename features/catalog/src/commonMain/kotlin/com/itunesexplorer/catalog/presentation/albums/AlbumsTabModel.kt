package com.itunesexplorer.catalog.presentation.albums

import com.itunesexplorer.common.mvi.MviViewModel
import com.itunesexplorer.common.mvi.ViewEffect
import com.itunesexplorer.common.mvi.ViewIntent
import com.itunesexplorer.common.mvi.ViewState
import cafe.adriel.voyager.core.model.screenModelScope
import com.itunesexplorer.catalog.shared.data.api.CatalogApi
import com.itunesexplorer.catalog.shared.data.models.*
import com.itunesexplorer.currency.domain.CurrencyFormatter
import com.itunesexplorer.network.api.ITunesApi
import com.itunesexplorer.network.models.MusicGenre
import com.itunesexplorer.settings.country.CountryManager
import kotlinx.coroutines.flow.drop
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AlbumsViewState(
    val recommendations: List<RssFeedEntry> = emptyList(),
    val selectedGenre: MusicGenre = MusicGenre.ALL,
    val isLoading: Boolean = false,
    val error: String? = null
) : ViewState

sealed class AlbumsIntent : ViewIntent {
    data object LoadRecommendations : AlbumsIntent()
    data class SelectGenre(val genre: MusicGenre) : AlbumsIntent()
    data object Retry : AlbumsIntent()
}

sealed class AlbumsEffect : ViewEffect {
    data class ShowError(val message: String) : AlbumsEffect()
}

class AlbumsTabModel(
    private val catalogApi: CatalogApi,
    private val iTunesApi: ITunesApi
) : MviViewModel<AlbumsViewState, AlbumsIntent, AlbumsEffect>(
    initialState = AlbumsViewState()
) {

    init {
        onAction(AlbumsIntent.LoadRecommendations)

        // Observe country changes and reload albums
        screenModelScope.launch {
            CountryManager.currentCountry
                .drop(1)
                .collect { country ->
                    if (country != null) {
                        onAction(AlbumsIntent.LoadRecommendations)
                    }
                }
        }
    }

    override fun onAction(intent: AlbumsIntent) {
        when (intent) {
            is AlbumsIntent.LoadRecommendations -> loadRecommendations()
            is AlbumsIntent.SelectGenre -> selectGenre(intent.genre)
            is AlbumsIntent.Retry -> {
                if (state.value.selectedGenre == MusicGenre.ALL) {
                    loadRecommendations()
                } else {
                    loadAlbumsByGenre(state.value.selectedGenre)
                }
            }
        }
    }

    private fun selectGenre(genre: MusicGenre) {
        mutableState.update { it.copy(selectedGenre = genre) }

        if (genre == MusicGenre.ALL) {
            loadRecommendations()
        } else {
            loadAlbumsByGenre(genre)
        }
    }

    private fun loadRecommendations() {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val country = CountryManager.getCurrentCountryCode() ?: "us"
                val response = catalogApi.topAlbums(
                    limit = 30,
                    country = country.lowercase()
                )
                mutableState.update {
                    it.copy(
                        isLoading = false,
                        recommendations = response.feed.entry
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
                sendEffect(AlbumsEffect.ShowError(errorMessage))
            }
        }
    }

    private fun loadAlbumsByGenre(genre: MusicGenre) {
        screenModelScope.launch {
            mutableState.update { it.copy(isLoading = true, error = null) }

            try {
                val country = CountryManager.getCurrentCountryCode()
                val lang = com.itunesexplorer.settings.language.LanguageManager.getITunesLanguageCode()
                val response = iTunesApi.searchByGenre(
                    genre = genre.searchTerm,
                    limit = 30,
                    lang = lang,
                    country = country
                )

                val entries = response.results.mapNotNull { item ->
                    val collectionId = item.collectionId
                    val collectionName = item.collectionName
                    val price = item.collectionPrice?.let { priceValue ->
                        val currencyCode = item.currency ?: "USD"
                        RssPrice(
                            label = CurrencyFormatter.format(priceValue, currencyCode),
                            attributes = RssPrice.RssPriceAttributes(
                                amount = priceValue.toString(),
                                currency = currencyCode
                            )
                        )
                    }

                    if (collectionId != null && collectionName != null) {
                        RssFeedEntry(
                            id = RssId(
                                label = collectionId.toString(),
                                attributes = RssId.RssIdAttributes(
                                    imId = collectionId.toString()
                                )
                            ),
                            imName = RssLabel(collectionName),
                            imImage = listOfNotNull(
                                item.artworkUrl60?.let { RssImage(it, RssImage.RssImageAttributes("60")) },
                                item.artworkUrl100?.let { RssImage(it, RssImage.RssImageAttributes("100")) }
                            ),
                            imArtist = item.artistName?.let { RssArtist(it) },
                            category = RssCategory(
                                attributes = RssCategory.RssCategoryAttributes(
                                    label = item.primaryGenreName ?: "Music"
                                )
                            ),
                            imReleaseDate = item.releaseDate?.let {
                                RssReleaseDate(
                                    attributes = RssReleaseDate.RssReleaseDateAttributes(it)
                                )
                            },
                            link = RssLink(
                                attributes = RssLink.RssLinkAttributes(
                                    href = item.collectionViewUrl ?: ""
                                )
                            ),
                            imPrice = price,
                            title = RssLabel(collectionName)
                        )
                    } else null
                }

                mutableState.update {
                    it.copy(
                        isLoading = false,
                        recommendations = entries
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
                sendEffect(AlbumsEffect.ShowError(errorMessage))
            }
        }
    }
}
